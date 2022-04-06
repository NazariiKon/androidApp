using AutoMapper;
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
        private readonly AppEFContext _context;
        private readonly IMapper _mapper;

        public ProductsController(AppEFContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        [HttpPost("create")]
        public IActionResult Create(CreateProductViewModel model)
        {
            var img = ImageWorker.FromBase64StringToImage(model.Image);
            string randomFilename = Path.GetRandomFileName() + ".jpeg";
            var dir = Path.Combine(Directory.GetCurrentDirectory(), "uploads", randomFilename);
            img.Save(dir, ImageFormat.Jpeg);

            var product = _mapper.Map<ProductEntity>(model);
            _context.Products.Add(product);
            _context.SaveChanges();

            return Ok(new { id = product.Id });
        }

        [HttpGet("list")]
        public IActionResult Index()
        {
            var list = _context.Products
                    .Select(x => _mapper.Map<ProductItemViewModel>(x))
                    .ToList();
            return Ok(list);
        }
    }
}
