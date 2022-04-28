using Shop.Data;
using Shop.Data.Entities.Identity;
using Shop.Helpers;
using Shop.Models;
using Shop.Services;
using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Drawing.Imaging;


namespace Shop.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        private readonly IMapper _mapper;
        private readonly IJwtTokenService _jwtTokenService;
        private readonly UserManager<AppUser> _userManager;
        private readonly AppEFContext _context;
        public AccountController(UserManager<AppUser> userManager,
            IJwtTokenService jwtTokenService, IMapper mapper,
            AppEFContext context)
        {
            _userManager = userManager;
            _mapper = mapper;
            _jwtTokenService = jwtTokenService;
            _context = context;
        }

        [HttpPost]
        [Route("register")]
        public async Task<IActionResult> Register([FromBody] RegisterViewModel model) // приймає модель з андроіда
        {
            var img = ImageWorker.FromBase64StringToImage(model.Photo); // з base64 в var
            string randomFilename = Path.GetRandomFileName() + ".jpeg"; 
            var dir = Path.Combine(Directory.GetCurrentDirectory(), "uploads", randomFilename);
            img.Save(dir, ImageFormat.Jpeg); // зберігає фото в папці з рандомною назвою
            var user = _mapper.Map<AppUser>(model);  // мапи моделі в юзера
            user.Photo = randomFilename; // присвоює фотку
            var result = await _userManager.CreateAsync(user, model.Password); // створює користувача

            if (!result.Succeeded)
                return BadRequest(new { errors = result.Errors });


            return Ok(new { token = _jwtTokenService.CreateToken(user) }); // повертає токен нового юзера
        }

        [HttpGet]
        [Route("users")]
        public async Task<IActionResult> Users() // повернення всіх зареганих юзерів з затримкою 2000
        {
            Thread.Sleep(2000);
            var list = await _context.Users.Select(x => _mapper.Map<UserItemViewModel>(x))
                .AsQueryable().ToListAsync();

            return Ok(list);
        }
        /// <summary>
        /// Вхід на сайт
        /// </summary>
        /// <param name="model">Подель із даними</param>
        /// <returns>Повертає токен авторизації</returns>
        /// <remarks>Awesomeness!</remarks>
        /// <response code="200">Login user</response>
        /// <response code="400">Login has missing/invalid values</response>
        /// <response code="500">Oops! Can't create your login right now</response>
        [HttpPost]
        [Route("login")]
        [ProducesResponseType(StatusCodes.Status200OK, Type = typeof(TokenResponceViewModel))]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        public async Task<IActionResult> Login([FromBody] LoginViewModel model)
        {
            try
            {
                var user = await _userManager.FindByEmailAsync(model.Email); // шукає юзера по пошті
                if (user != null)
                {

                    if (await _userManager.CheckPasswordAsync(user, model.Password)) // чекає пароль
                    {
                        return Ok(new TokenResponceViewModel { token = _jwtTokenService.CreateToken(user) }); // вертає токен
                    }
                }
                return BadRequest(new { error = "Користувача не знайдено" });
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = "Помилка на сервері. " + ex.Message });
            }
        }

        [HttpPost]
        [Route("delete")]
        public async Task<IActionResult> Delete(string Id) // приймає id
        {
            try
            {
                var user = await _userManager.FindByIdAsync(Id);
                if (user != null)
                {
                    var res = await _userManager.DeleteAsync(user);
                    if (res.Succeeded)
                    {
                        var filePath = Path.Combine(Directory.GetCurrentDirectory(), "uploads", user.Photo);
                        if (System.IO.File.Exists(filePath))
                            System.IO.File.Delete(filePath); // видалення фотографії
                        return Ok(res);
                    }
                    else return BadRequest(new { error = "Помилка при видаленні" });
                }
                return BadRequest(new { error = "Невірне ID" });
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = "Помилка на сервері. " + ex.Message });
            }
        }

        [HttpPost]
        [Route("edit")]
        public async Task<IActionResult> Edit(UserItemViewModel model)
        {
            try
            {
                var user = await _userManager.FindByIdAsync(model.Id.ToString());
                if (user != null)
                {
                    user.Email = model.Email;
                    user.FirstName = model.FirstName;
                    user.SecondName = model.SecondName;
                    user.Phone = model.Phone;
                    if ("/images/" + user.Photo != model.Photo.ToString())
                    {
                        var img = ImageWorker.FromBase64StringToImage(model.Photo); // з base64 в var
                        var dir = Path.Combine(Directory.GetCurrentDirectory(), "uploads", user.Photo);
                        img.Save(dir, ImageFormat.Jpeg); // зберігає фото в папці з рандомною назвою
                    }
                   
                    var res = await _userManager.UpdateAsync(user);
                    if (res.Succeeded)
                        return Ok(res);

                    else return BadRequest(new { error = "Помилка при видаленні" });
                }
                return BadRequest(new { error = "Невірне ID" });
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = "Помилка на сервері. " + ex.Message });
            }
        }
    }
}
