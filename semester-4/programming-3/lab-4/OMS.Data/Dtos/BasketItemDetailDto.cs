namespace OMS.Data.Dtos
{
    public class BasketItemDetailDto
    {
        public int IdBasketItem { get; set; }
        public short IdProduct { get; set; }
        public string ProductName { get; set; } = string.Empty;
        public byte Quantity { get; set; }
        public decimal UnitPrice { get; set; }
        public decimal LineTotal => Quantity * UnitPrice;
    }
}