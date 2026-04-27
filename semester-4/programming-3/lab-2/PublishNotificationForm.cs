using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Lab2
{
    public partial class PublishNotificationForm : Form
    {
        private readonly NotificationCenter _center;

        public PublishNotificationForm(NotificationCenter center)
        {
            InitializeComponent();
            _center = center;
        }

        private void btnPublish_Click(object sender, EventArgs e)
        {
            var msg = txtNotificationContent.Text.Trim();
            if (string.IsNullOrWhiteSpace(msg))
            {
                MessageBox.Show("Please enter notification content.", "Missing", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            _center.Publish(msg);
            MessageBox.Show("Notification published to all subscribers.", "Published", MessageBoxButtons.OK, MessageBoxIcon.Information);
            Close();
        }

        private void btnExit_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}
