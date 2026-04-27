
namespace Lab2
{
    partial class ManageSubscriptionForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        private System.Windows.Forms.CheckBox chkEmail;
        private System.Windows.Forms.TextBox txtEmail;
        private System.Windows.Forms.Label lblEmailError;

        private System.Windows.Forms.CheckBox chkSMS;
        private System.Windows.Forms.TextBox txtMobile;
        private System.Windows.Forms.Label lblMobileError;

        private System.Windows.Forms.Button btnSubscribe;
        private System.Windows.Forms.Button btnUnsubscribe;
        private System.Windows.Forms.Button btnCancel;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.chkEmail = new System.Windows.Forms.CheckBox();
            this.txtEmail = new System.Windows.Forms.TextBox();
            this.lblEmailError = new System.Windows.Forms.Label();
            this.chkSMS = new System.Windows.Forms.CheckBox();
            this.txtMobile = new System.Windows.Forms.TextBox();
            this.lblMobileError = new System.Windows.Forms.Label();
            this.btnSubscribe = new System.Windows.Forms.Button();
            this.btnUnsubscribe = new System.Windows.Forms.Button();
            this.btnCancel = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // chkEmail
            // 
            this.chkEmail.AutoSize = true;
            this.chkEmail.Location = new System.Drawing.Point(21, 24);
            this.chkEmail.Name = "chkEmail";
            this.chkEmail.Size = new System.Drawing.Size(146, 17);
            this.chkEmail.TabIndex = 0;
            this.chkEmail.Text = "Notification Sent by Email";
            this.chkEmail.UseVisualStyleBackColor = true;
            this.chkEmail.CheckedChanged += new System.EventHandler(this.chkEmail_CheckedChanged);
            // 
            // txtEmail
            // 
            this.txtEmail.Location = new System.Drawing.Point(206, 23);
            this.txtEmail.Name = "txtEmail";
            this.txtEmail.Size = new System.Drawing.Size(189, 20);
            this.txtEmail.TabIndex = 1;
            // 
            // lblEmailError
            // 
            this.lblEmailError.AutoSize = true;
            this.lblEmailError.ForeColor = System.Drawing.Color.Red;
            this.lblEmailError.Location = new System.Drawing.Point(206, 45);
            this.lblEmailError.Name = "lblEmailError";
            this.lblEmailError.Size = new System.Drawing.Size(0, 13);
            this.lblEmailError.TabIndex = 2;
            // 
            // chkSMS
            // 
            this.chkSMS.AutoSize = true;
            this.chkSMS.Location = new System.Drawing.Point(21, 76);
            this.chkSMS.Name = "chkSMS";
            this.chkSMS.Size = new System.Drawing.Size(145, 17);
            this.chkSMS.TabIndex = 3;
            this.chkSMS.Text = "Notification Sent By SMS";
            this.chkSMS.UseVisualStyleBackColor = true;
            this.chkSMS.CheckedChanged += new System.EventHandler(this.chkSMS_CheckedChanged);
            // 
            // txtMobile
            // 
            this.txtMobile.Location = new System.Drawing.Point(206, 75);
            this.txtMobile.Name = "txtMobile";
            this.txtMobile.Size = new System.Drawing.Size(189, 20);
            this.txtMobile.TabIndex = 4;
            // 
            // lblMobileError
            // 
            this.lblMobileError.AutoSize = true;
            this.lblMobileError.ForeColor = System.Drawing.Color.Red;
            this.lblMobileError.Location = new System.Drawing.Point(206, 97);
            this.lblMobileError.Name = "lblMobileError";
            this.lblMobileError.Size = new System.Drawing.Size(0, 13);
            this.lblMobileError.TabIndex = 5;
            // 
            // btnSubscribe
            // 
            this.btnSubscribe.Location = new System.Drawing.Point(69, 139);
            this.btnSubscribe.Name = "btnSubscribe";
            this.btnSubscribe.Size = new System.Drawing.Size(77, 26);
            this.btnSubscribe.TabIndex = 6;
            this.btnSubscribe.Text = "Subscribe";
            this.btnSubscribe.UseVisualStyleBackColor = true;
            this.btnSubscribe.Click += new System.EventHandler(this.btnSubscribe_Click);
            // 
            // btnUnsubscribe
            // 
            this.btnUnsubscribe.Location = new System.Drawing.Point(163, 139);
            this.btnUnsubscribe.Name = "btnUnsubscribe";
            this.btnUnsubscribe.Size = new System.Drawing.Size(77, 26);
            this.btnUnsubscribe.TabIndex = 7;
            this.btnUnsubscribe.Text = "Unsubscribe";
            this.btnUnsubscribe.UseVisualStyleBackColor = true;
            this.btnUnsubscribe.Click += new System.EventHandler(this.btnUnsubscribe_Click);
            // 
            // btnCancel
            // 
            this.btnCancel.Location = new System.Drawing.Point(257, 139);
            this.btnCancel.Name = "btnCancel";
            this.btnCancel.Size = new System.Drawing.Size(77, 26);
            this.btnCancel.TabIndex = 8;
            this.btnCancel.Text = "Cancel";
            this.btnCancel.UseVisualStyleBackColor = true;
            this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
            // 
            // ManageSubscriptionForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(429, 186);
            this.Controls.Add(this.btnCancel);
            this.Controls.Add(this.btnUnsubscribe);
            this.Controls.Add(this.btnSubscribe);
            this.Controls.Add(this.lblMobileError);
            this.Controls.Add(this.txtMobile);
            this.Controls.Add(this.chkSMS);
            this.Controls.Add(this.lblEmailError);
            this.Controls.Add(this.txtEmail);
            this.Controls.Add(this.chkEmail);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "ManageSubscriptionForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Manage Subscription";
            this.Load += new System.EventHandler(this.ManageSubscriptionForm_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion
    }
}