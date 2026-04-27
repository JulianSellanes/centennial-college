using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Mail;
using System.Text.RegularExpressions;

namespace Lab2
{
    public class Validators
    {
        public static bool IsValidEmail(string email)
        {
            if (string.IsNullOrWhiteSpace(email)) return false;

            try
            {
                var addr = new MailAddress(email);
                return addr.Address == email.Trim();
            }
            catch
            {
                return false;
            }
        }

        public static bool IsValidMobile(string mobile)
        {
            if (string.IsNullOrWhiteSpace(mobile)) return false;
            return Regex.IsMatch(mobile.Trim(), @"^\d{3}-\d{3}-\d{4}$");
        }
    }
}
