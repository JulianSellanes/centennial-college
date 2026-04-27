
namespace Lab2
{
    partial class PublishNotificationForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        private System.Windows.Forms.Label lblNotificationContent;
        private System.Windows.Forms.TextBox txtNotificationContent;
        private System.Windows.Forms.Button btnPublish;
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
            this.lblNotificationContent = new System.Windows.Forms.Label();
            this.txtNotificationContent = new System.Windows.Forms.TextBox();
            this.btnPublish = new System.Windows.Forms.Button();
            this.btnExit = new System.Windows.Forms.Button();
            this.SuspendLayout();

            // lblNotificationContent
            this.lblNotificationContent.AutoSize = true;
            this.lblNotificationContent.Location = new System.Drawing.Point(24, 30);
            this.lblNotificationContent.Name = "lblNotificationContent";
            this.lblNotificationContent.Size = new System.Drawing.Size(118, 15);
            this.lblNotificationContent.TabIndex = 0;
            this.lblNotificationContent.Text = "Notification Content";

            // txtNotificationContent
            this.txtNotificationContent.Location = new System.Drawing.Point(160, 27);
            this.txtNotificationContent.Name = "txtNotificationContent";
            this.txtNotificationContent.Size = new System.Drawing.Size(260, 23);
            this.txtNotificationContent.TabIndex = 1;

            // btnPublish
            this.btnPublish.Location = new System.Drawing.Point(160, 75);
            this.btnPublish.Name = "btnPublish";
            this.btnPublish.Size = new System.Drawing.Size(110, 35);
            this.btnPublish.TabIndex = 2;
            this.btnPublish.Text = "Publish";
            this.btnPublish.UseVisualStyleBackColor = true;
            this.btnPublish.Click += new System.EventHandler(this.btnPublish_Click);

            // btnExit
            this.btnExit.Location = new System.Drawing.Point(310, 75);
            this.btnExit.Name = "btnExit";
            this.btnExit.Size = new System.Drawing.Size(110, 35);
            this.btnExit.TabIndex = 3;
            this.btnExit.Text = "Exit";
            this.btnExit.UseVisualStyleBackColor = true;
            this.btnExit.Click += new System.EventHandler(this.btnExit_Click);

            // PublishNotificationForm
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(450, 135);
            this.Controls.Add(this.btnExit);
            this.Controls.Add(this.btnPublish);
            this.Controls.Add(this.txtNotificationContent);
            this.Controls.Add(this.lblNotificationContent);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "PublishNotificationForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Publish Notification";
            this.ResumeLayout(false);
            this.PerformLayout();
        }

        #endregion
    }
}