using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using OMS.Data.Dtos;
using OMS.Data.Models;

namespace OMS.Data.Services
{
    public class OmsService
    {
        private readonly OMSContext _context;

        public OmsService()
        {
            _context = new OMSContext();
        }

        public OmsService(OMSContext context)
        {
            _context = context;
        }

        public async Task<List<BasketComboItemDto>> GetBasketsAsync()
        {
            var baskets = await (
                from b in _context.Baskets
                join s in _context.Shoppers on b.IdShopper equals s.IdShopper
                orderby s.Email, b.IdBasket
                select new BasketComboItemDto
                {
                    IdBasket = b.IdBasket,
                    ShopperEmail = s.Email
                }
            ).ToListAsync();

            return baskets;
        }

        public async Task<List<BasketItemDetailDto>> GetBasketItemsByBasketIdAsync(int basketId)
        {
            var items = await (
                from bi in _context.BasketItems
                join p in _context.Products on bi.IdProduct equals p.IdProduct
                where bi.IdBasket == basketId
                orderby bi.IdBasketItem
                select new BasketItemDetailDto
                {
                    IdBasketItem = bi.IdBasketItem,
                    IdProduct = p.IdProduct,
                    ProductName = p.ProductName,
                    Quantity = bi.Quantity ?? 0,
                    UnitPrice = p.Price ?? 0m
                }
            ).ToListAsync();

            return items;
        }

        public async Task<List<ProductComboItemDto>> GetProductsAsync()
        {
            var products = await _context.Products
                .OrderBy(p => p.ProductName)
                .Select(p => new ProductComboItemDto
                {
                    IdProduct = p.IdProduct,
                    ProductName = p.ProductName,
                    Price = p.Price ?? 0m
                })
                .ToListAsync();

            return products;
        }

        public async Task<int> AddBasketItemAsync(AddBasketItemDto dto)
        {
            if (dto == null)
                throw new ArgumentNullException(nameof(dto));

            if (dto.Quantity <= 0)
                throw new ArgumentException("Quantity must be greater than 0.");

            var basketExists = await _context.Baskets
                .AnyAsync(b => b.IdBasket == dto.IdBasket);

            if (!basketExists)
                throw new Exception("Selected basket does not exist.");

            var product = await _context.Products
                .FirstOrDefaultAsync(p => p.IdProduct == dto.IdProduct);

            if (product == null)
                throw new Exception("Selected product does not exist.");

            var currentMaxId = await _context.BasketItems
                .Select(x => (int?)x.IdBasketItem)
                .MaxAsync() ?? 0;

            int newId = currentMaxId + 1;

            var newBasketItem = new BasketItem
            {
                IdBasketItem = newId,
                IdBasket = dto.IdBasket,
                IdProduct = dto.IdProduct,
                Quantity = dto.Quantity
            };

            _context.BasketItems.Add(newBasketItem);

            var basket = await _context.Baskets.FirstAsync(b => b.IdBasket == dto.IdBasket);

            byte currentBasketQty = basket.Quantity ?? 0;
            int updatedQty = currentBasketQty + dto.Quantity;

            if (updatedQty > byte.MaxValue)
                throw new Exception("Basket quantity exceeded the maximum value for a byte.");

            basket.Quantity = (byte)updatedQty;
            basket.SubTotal = (basket.SubTotal ?? 0m) + ((product.Price ?? 0m) * dto.Quantity);

            await _context.SaveChangesAsync();

            return newId;
        }
    }
}