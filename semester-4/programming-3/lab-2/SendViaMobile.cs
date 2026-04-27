using System;
using System.Diagnostics;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab2
{
    public class SendViaMobile
    {
        public string Mobile { get; }

        public SendViaMobile(string mobile)
        {
            Mobile = mobile;
        }

        public void Subscribe(NotificationCenter center)
        {
            center.NotifyAll += Send;
        }

        public void Unsubscribe(NotificationCenter center)
        {
            center.NotifyAll -= Send;
        }

        private void Send(string message)
        {
            Debug.WriteLine($"[SMS] To {Mobile}: {message}");
        }
    }
}
