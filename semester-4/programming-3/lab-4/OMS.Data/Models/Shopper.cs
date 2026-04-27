using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

#nullable disable

namespace OMS.Data.Models
{
    [Table("Shopper")]
    public partial class Shopper
    {
        public Shopper()
        {
            Baskets = new HashSet<Basket>();
        }

        [Key]
        [Column("idShopper")]
        public int IdShopper { get; set; }
        [Required]
        [StringLength(25)]
        public string Email { get; set; }
        [StringLength(15)]
        public string FirstName { get; set; }
        [StringLength(20)]
        public string LastName { get; set; }
        [StringLength(40)]
        public string Address { get; set; }
        [StringLength(20)]
        public string City { get; set; }
        [StringLength(20)]
        public string StateProvince { get; set; }
        [StringLength(20)]
        public string Country { get; set; }
        [StringLength(15)]
        public string ZipCode { get; set; }

        [InverseProperty(nameof(Basket.IdShopperNavigation))]
        public virtual ICollection<Basket> Baskets { get; set; }
    }
}
