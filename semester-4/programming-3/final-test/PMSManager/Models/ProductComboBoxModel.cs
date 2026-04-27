using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PMSManager.Models
{
    public class ProductComboBoxModel
    {
        public string Code { get; set; }
        public string Name { get; set; }

        public ProductComboBoxModel(string code, string name)
        {
            Code = code;
            Name = name;
        }
    }
}
