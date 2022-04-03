using AutoMapper;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Shop.Data;
using Shop.Data.Entities;
using Shop.Models;

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
            var product = _mapper.Map<ProductEntity>(model);
            _context.Products.Add(product);
            _context.SaveChanges();
            return Ok();
        }

        [HttpGet("all")]
        public List<ProductEntity> Read()
        {
            return _context.Products.ToList();
        }
    }
}
