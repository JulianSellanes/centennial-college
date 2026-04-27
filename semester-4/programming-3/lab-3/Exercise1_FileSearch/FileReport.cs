using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Exercise1_FileSearch
{
    public class FileReport
    {
        public string FileName { get; set; }
        public string FullPath { get; set; }
        public double SizeKB { get; set; }
        public string LastModified { get; set; }
    }
}