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
    public partial class MainForm : Form
    {
        private readonly NotificationCenter _center = new NotificationCenter();

        private readonly List<SendViaEmail> _emailSubs = new List<SendViaEmail>();
        private readonly List<SendViaMobile> _smsSubs = new List<SendViaMobile>();

        public MainForm()
        {
            InitializeComponent();
            UpdatePublishButton();
        }

        private void UpdatePublishButton()
        {
            btnPublishNotification.Enabled = (_emailSubs.Count + _smsSubs.Count) > 0;
        }

        private void btnManageSubscription_Click(object sender, EventArgs e)
        {
            using (var f = new ManageSubscriptionForm(_center, _emailSubs, _smsSubs))
            {
                f.ShowDialog(this);
            }
            UpdatePublishButton();
        }

        private void btnPublishNotification_Click(object sender, EventArgs e)
        {
            using (var f = new PublishNotificationForm(_center))
            {
                f.ShowDialog(this);
            }
        }

        private void btnExit_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}
