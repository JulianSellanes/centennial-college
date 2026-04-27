using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PMSManager.Models
{
    public class SalesDetails
    {
        public string Code { get; set; }
        public string ProductName { get; set; }
        public int Amount { get; set; }
        public DateTime TransactionDate { get; set; }

        public SalesDetails(string code, string productName, int amount, DateTime transactionDate)
        {
            Code = code;
            ProductName = productName;
            Amount = amount;
            TransactionDate = transactionDate;
        }
    }
}
