using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Exercise2_RestaurantBill.Models
{
    public class RestaurantMenuItem
    {
        public string Name { get; set; }
        public string Category { get; set; }
        public decimal Price { get; set; }

        public override string ToString()
        {
            return Name;
        }
    }
}