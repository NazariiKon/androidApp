using Microsoft.EntityFrameworkCore;
using Shop.Data.Entities;

namespace Shop.Data
{
    public class AppEFContext : DbContext
    {
        public AppEFContext(DbContextOptions<AppEFContext> options) :
           base(options)
        {
        }

        public DbSet<ProductEntity> Products { get; set; }
    }
}
