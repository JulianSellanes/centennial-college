using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PMSManager.Models
{
    public class SalesPersonComboBoxModel
    {
        public string Id { get; set; }
        public string Fname { get; set; }
        public string Lname { get; set; }

        public SalesPersonComboBoxModel(string id, string fname, string lname)
        {
            Id = id;
            Fname = fname;
            Lname = lname;
        }
    }
}
