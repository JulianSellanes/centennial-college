using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab2
{
    public class NotificationCenter
    {
        // Explicit delegate type
        public delegate void NotificationHandler(string message);

        // Multicast delegate list
        public NotificationHandler NotifyAll;

        public void Publish(string message)
        {
            NotifyAll?.Invoke(message);
        }
    }
}
