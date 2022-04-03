using AutoMapper;
using Shop.Data.Entities;
using Shop.Models;

namespace Shop.Mapper
{
	public class AppMappingProfile : Profile
	{
		public AppMappingProfile()
		{
			CreateMap<CreateProductViewModel, ProductEntity>();
		}
	}
}
