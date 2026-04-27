using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

#nullable disable

namespace OMS.Data.Models
{
    [Table("Basket")]
    public partial class Basket
    {
        public Basket()
        {
            BasketItems = new HashSet<BasketItem>();
        }

        [Key]
        [Column("idBasket")]
        public int IdBasket { get; set; }
        [Column("idShopper")]
        public int? IdShopper { get; set; }
        public byte? Quantity { get; set; }
        [Column(TypeName = "decimal(7, 2)")]
        public decimal? SubTotal { get; set; }
        [Column(TypeName = "datetime")]
        public DateTime OrderDate { get; set; }

        [ForeignKey(nameof(IdShopper))]
        [InverseProperty(nameof(Shopper.Baskets))]
        public virtual Shopper IdShopperNavigation { get; set; }
        [InverseProperty(nameof(BasketItem.IdBasketNavigation))]
        public virtual ICollection<BasketItem> BasketItems { get; set; }
    }
}
