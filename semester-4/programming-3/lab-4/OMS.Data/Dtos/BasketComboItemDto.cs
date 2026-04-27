using System;

namespace OMS.Data.Dtos
{
    public class BasketComboItemDto
    {
        public int IdBasket { get; set; }
        public string ShopperEmail { get; set; } = string.Empty;

        public string DisplayText => $"{ShopperEmail} - Basket #{IdBasket}";
    }
}