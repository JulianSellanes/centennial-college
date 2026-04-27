namespace OMS.Data.Dtos
{
    public class AddBasketItemDto
    {
        public int IdBasket { get; set; }
        public short IdProduct { get; set; }
        public byte Quantity { get; set; }
    }
}