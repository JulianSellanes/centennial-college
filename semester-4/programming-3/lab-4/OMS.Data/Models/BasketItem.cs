using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

#nullable disable

namespace OMS.Data.Models
{
    [Table("BasketItem")]
    public partial class BasketItem
    {
        [Key]
        [Column("idBasketItem")]
        public int IdBasketItem { get; set; }
        [Column("idProduct")]
        public short? IdProduct { get; set; }
        public byte? Quantity { get; set; }
        [Column("idBasket")]
        public int? IdBasket { get; set; }

        [ForeignKey(nameof(IdBasket))]
        [InverseProperty(nameof(Basket.BasketItems))]
        public virtual Basket IdBasketNavigation { get; set; }
        [ForeignKey(nameof(IdProduct))]
        [InverseProperty(nameof(Product.BasketItems))]
        public virtual Product IdProductNavigation { get; set; }
    }
}
