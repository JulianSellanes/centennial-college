using System;
using System.Collections.Generic;

namespace PMSModels
{
    public partial class Product
    {
        public Product()
        {
            SalesTransactions = new HashSet<SalesTransaction>();
        }

        public string Code { get; set; }
        public string Name { get; set; }
        public int Inventory { get; set; }
        public decimal Price { get; set; }

        public virtual ICollection<SalesTransaction> SalesTransactions { get; set; }
    }
}
