namespace OMS.Data.Dtos
{
    public class ProductComboItemDto
    {
        public short IdProduct { get; set; }
        public string ProductName { get; set; } = string.Empty;
        public decimal Price { get; set; }

        public string DisplayText => $"{ProductName} (${Price:F2})";
    }
}