using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Shop.Data;
using Shop.Data.Entities;
using Shop.Helpers;
using Shop.Models;
using System.Drawing.Imaging;

namespace Shop.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductsController : ControllerBase
    {
        private readonly IMapper _mapper;
        private readonly AppEFContext _context;
        public ProductsController(IMapper mapper, AppEFContext context)
        {
            _mapper = mapper;
            _context = context;
        }

        [HttpPost("create")]
        [Authorize]
        [RequestSizeLimit(100 * 1024 * 1024)]     // set the maximum file size limit to 100 MB
        public IActionResult Create(CreateProductViewModel model)
        {
            var img = ImageWorker.FromBase64StringToImage(model.Image);
            string randomFilename = Path.GetRandomFileName() + ".jpeg";
            var dir = Path.Combine(Directory.GetCurrentDirectory(), "uploads", randomFilename);
            img.Save(dir, ImageFormat.Jpeg);

            ProductEntity product = _mapper.Map<ProductEntity>(model); // мап моделі в продукт
            product.Image = randomFilename;
            _context.Products.Add(product);
            _context.SaveChanges();

            return Ok(new { id=product.Id }); // вертає айдішку
        }
        [HttpGet("list")]
        public IActionResult Index() // вертає ліст продуктів з затримкой 2000
        {
            //Thread.Sleep(2000); 
            var list = _context.Products
                    .Select(x=>_mapper.Map<ProductItemViewModel>(x))
                    .ToList();
            return Ok(list);
        }
    }
}
