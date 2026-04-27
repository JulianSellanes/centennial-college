using System;
using System.Collections.Generic;

namespace PMSModels
{
    public partial class Employee
    {
        public Employee()
        {
            SalesTransactions = new HashSet<SalesTransaction>();
        }

        public string Id { get; set; }
        public string Fname { get; set; }
        public string Lname { get; set; }
        public string Email { get; set; }

        public virtual ICollection<SalesTransaction> SalesTransactions { get; set; }
    }
}
