using System;
using System.Collections.Generic;

namespace PMSModels
{
    public partial class SalesTransaction
    {
        public string EmployeeId { get; set; }
        public string ProductCode { get; set; }
        public int Amount { get; set; }
        public DateTime SaleDate { get; set; }

        public virtual Employee Employee { get; set; }
        public virtual Product ProductCodeNavigation { get; set; }
    }
}
