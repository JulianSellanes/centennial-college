using System;
using System.Diagnostics;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab2
{
    public class SendViaEmail
    {
        public string Email { get; }

        public SendViaEmail(string email)
        {
            Email = email;
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
            Debug.WriteLine($"[EMAIL] To {Email}: {message}");
        }
    }
}
