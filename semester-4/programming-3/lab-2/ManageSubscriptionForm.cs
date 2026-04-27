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
    public partial class ManageSubscriptionForm : Form
    {
        private readonly NotificationCenter _center;
        private readonly List<SendViaEmail> _emailSubs;
        private readonly List<SendViaMobile> _smsSubs;

        public ManageSubscriptionForm(NotificationCenter center, List<SendViaEmail> emailSubs, List<SendViaMobile> smsSubs)
        {
            InitializeComponent();

            _center = center;
            _emailSubs = emailSubs;
            _smsSubs = smsSubs;

            txtEmail.Enabled = chkEmail.Checked;
            txtMobile.Enabled = chkSMS.Checked;

            lblEmailError.Text = "";
            lblMobileError.Text = "";
        }

        private void chkEmail_CheckedChanged(object sender, EventArgs e)
        {
            txtEmail.Enabled = chkEmail.Checked;
            if (!chkEmail.Checked) lblEmailError.Text = "";
        }

        private void chkSMS_CheckedChanged(object sender, EventArgs e)
        {
            txtMobile.Enabled = chkSMS.Checked;
            if (!chkSMS.Checked) lblMobileError.Text = "";
        }

        private void btnSubscribe_Click(object sender, EventArgs e)
        {
            lblEmailError.Text = "";
            lblMobileError.Text = "";

            bool didAnything = false;

            // Email subscribe
            if (chkEmail.Checked)
            {
                var email = txtEmail.Text.Trim();

                if (!Validators.IsValidEmail(email))
                {
                    lblEmailError.Text = "Invalid email address";
                    return;
                }

                bool exists = _emailSubs.Any(x => string.Equals(x.Email, email, StringComparison.OrdinalIgnoreCase));
                if (exists)
                {
                    MessageBox.Show("This email is already subscribed.", "Duplicate", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                }
                else
                {
                    var sub = new SendViaEmail(email);
                    _emailSubs.Add(sub);
                    sub.Subscribe(_center);
                    didAnything = true;
                }
            }

            // SMS subscribe
            if (chkSMS.Checked)
            {
                var mobile = txtMobile.Text.Trim();

                if (!Validators.IsValidMobile(mobile))
                {
                    lblMobileError.Text = "Invalid mobile format (xxx-xxx-xxxx)";
                    return;
                }

                bool exists = _smsSubs.Any(x => x.Mobile == mobile);
                if (exists)
                {
                    MessageBox.Show("This mobile is already subscribed.", "Duplicate", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                }
                else
                {
                    var sub = new SendViaMobile(mobile);
                    _smsSubs.Add(sub);
                    sub.Subscribe(_center);
                    didAnything = true;
                }
            }

            if (didAnything)
                MessageBox.Show("Subscribed successfully.", "OK", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        private void btnUnsubscribe_Click(object sender, EventArgs e)
        {
            lblEmailError.Text = "";
            lblMobileError.Text = "";

            bool didAnything = false;

            // Email unsubscribe
            if (chkEmail.Checked)
            {
                var email = txtEmail.Text.Trim();

                var existing = _emailSubs.FirstOrDefault(x =>
                    string.Equals(x.Email, email, StringComparison.OrdinalIgnoreCase));

                if (existing == null)
                {
                    MessageBox.Show("That email is not subscribed.", "Not found", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                }
                else
                {
                    existing.Unsubscribe(_center);
                    _emailSubs.Remove(existing);
                    didAnything = true;
                }
            }

            // SMS unsubscribe
            if (chkSMS.Checked)
            {
                var mobile = txtMobile.Text.Trim();

                var existing = _smsSubs.FirstOrDefault(x => x.Mobile == mobile);
                if (existing == null)
                {
                    MessageBox.Show("That mobile is not subscribed.", "Not found", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                }
                else
                {
                    existing.Unsubscribe(_center);
                    _smsSubs.Remove(existing);
                    didAnything = true;
                }
            }

            if (didAnything)
                MessageBox.Show("Unsubscribed successfully.", "OK", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void ManageSubscriptionForm_Load(object sender, EventArgs e)
        {

        }
    }
}
