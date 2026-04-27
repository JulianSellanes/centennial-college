using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

#nullable disable

namespace OMS.Data.Models
{
    [Table("Product")]
    public partial class Product
    {
        public Product()
        {
            BasketItems = new HashSet<BasketItem>();
        }

        [Key]
        [Column("idProduct")]
        public short IdProduct { get; set; }
        [StringLength(25)]
        public string ProductName { get; set; }
        [StringLength(100)]
        public string Description { get; set; }
        [Column(TypeName = "decimal(6, 2)")]
        public decimal? Price { get; set; }

        [InverseProperty(nameof(BasketItem.IdProductNavigation))]
        public virtual ICollection<BasketItem> BasketItems { get; set; }
    }
}
