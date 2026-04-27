
namespace Lab2
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        private System.Windows.Forms.Button btnManageSubscription;
        private System.Windows.Forms.Button btnPublishNotification;
        private System.Windows.Forms.Button btnExit;

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
            this.btnManageSubscription = new System.Windows.Forms.Button();
            this.btnPublishNotification = new System.Windows.Forms.Button();
            this.btnExit = new System.Windows.Forms.Button();
            this.SuspendLayout();

            // btnManageSubscription
            this.btnManageSubscription.Location = new System.Drawing.Point(24, 24);
            this.btnManageSubscription.Name = "btnManageSubscription";
            this.btnManageSubscription.Size = new System.Drawing.Size(160, 40);
            this.btnManageSubscription.TabIndex = 0;
            this.btnManageSubscription.Text = "Manage Subscription";
            this.btnManageSubscription.UseVisualStyleBackColor = true;
            this.btnManageSubscription.Click += new System.EventHandler(this.btnManageSubscription_Click);

            // btnPublishNotification
            this.btnPublishNotification.Enabled = false; // required: disabled when no subscribers
            this.btnPublishNotification.Location = new System.Drawing.Point(200, 24);
            this.btnPublishNotification.Name = "btnPublishNotification";
            this.btnPublishNotification.Size = new System.Drawing.Size(160, 40);
            this.btnPublishNotification.TabIndex = 1;
            this.btnPublishNotification.Text = "Publish Notification";
            this.btnPublishNotification.UseVisualStyleBackColor = true;
            this.btnPublishNotification.Click += new System.EventHandler(this.btnPublishNotification_Click);

            // btnExit
            this.btnExit.Location = new System.Drawing.Point(376, 24);
            this.btnExit.Name = "btnExit";
            this.btnExit.Size = new System.Drawing.Size(90, 40);
            this.btnExit.TabIndex = 2;
            this.btnExit.Text = "Exit";
            this.btnExit.UseVisualStyleBackColor = true;
            this.btnExit.Click += new System.EventHandler(this.btnExit_Click);

            // MainForm
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(490, 92);
            this.Controls.Add(this.btnExit);
            this.Controls.Add(this.btnPublishNotification);
            this.Controls.Add(this.btnManageSubscription);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "MainForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Notification Manager";
            this.ResumeLayout(false);
        }

        #endregion
    }
}