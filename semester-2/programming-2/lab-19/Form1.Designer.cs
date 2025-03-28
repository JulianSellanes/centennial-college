
namespace Lab19
{
    partial class Window
    {
        /// <summary>
        /// Variable del diseñador necesaria.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Limpiar los recursos que se estén usando.
        /// </summary>
        /// <param name="disposing">true si los recursos administrados se deben desechar; false en caso contrario.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Código generado por el Diseñador de Windows Forms

        /// <summary>
        /// Método necesario para admitir el Diseñador. No se puede modificar
        /// el contenido de este método con el editor de código.
        /// </summary>
        private void InitializeComponent()
        {
            this.grpOperations = new System.Windows.Forms.GroupBox();
            this.divRadio = new System.Windows.Forms.RadioButton();
            this.multRadio = new System.Windows.Forms.RadioButton();
            this.subRadio = new System.Windows.Forms.RadioButton();
            this.addRadio = new System.Windows.Forms.RadioButton();
            this.txtFirst = new System.Windows.Forms.TextBox();
            this.lblFirst = new System.Windows.Forms.Label();
            this.lblSecond = new System.Windows.Forms.Label();
            this.txtSecond = new System.Windows.Forms.TextBox();
            this.lblEquals = new System.Windows.Forms.Label();
            this.lblResult = new System.Windows.Forms.Label();
            this.txtResult = new System.Windows.Forms.TextBox();
            this.bttnCalculate = new System.Windows.Forms.Button();
            this.grpOperations.SuspendLayout();
            this.SuspendLayout();
            // 
            // grpOperations
            // 
            this.grpOperations.Controls.Add(this.divRadio);
            this.grpOperations.Controls.Add(this.multRadio);
            this.grpOperations.Controls.Add(this.subRadio);
            this.grpOperations.Controls.Add(this.addRadio);
            this.grpOperations.Location = new System.Drawing.Point(100, 22);
            this.grpOperations.Name = "grpOperations";
            this.grpOperations.Size = new System.Drawing.Size(99, 158);
            this.grpOperations.TabIndex = 0;
            this.grpOperations.TabStop = false;
            this.grpOperations.Text = "Operations";
            // 
            // divRadio
            // 
            this.divRadio.AutoSize = true;
            this.divRadio.Location = new System.Drawing.Point(36, 110);
            this.divRadio.Name = "divRadio";
            this.divRadio.Size = new System.Drawing.Size(30, 17);
            this.divRadio.TabIndex = 3;
            this.divRadio.TabStop = true;
            this.divRadio.Text = "/";
            this.divRadio.UseVisualStyleBackColor = true;
            this.divRadio.CheckedChanged += new System.EventHandler(this.divRadio_CheckedChanged);
            // 
            // multRadio
            // 
            this.multRadio.AutoSize = true;
            this.multRadio.Location = new System.Drawing.Point(35, 87);
            this.multRadio.Name = "multRadio";
            this.multRadio.Size = new System.Drawing.Size(30, 17);
            this.multRadio.TabIndex = 2;
            this.multRadio.TabStop = true;
            this.multRadio.Text = "x";
            this.multRadio.UseVisualStyleBackColor = true;
            this.multRadio.CheckedChanged += new System.EventHandler(this.multRadio_CheckedChanged);
            // 
            // subRadio
            // 
            this.subRadio.AutoSize = true;
            this.subRadio.Location = new System.Drawing.Point(35, 64);
            this.subRadio.Name = "subRadio";
            this.subRadio.Size = new System.Drawing.Size(28, 17);
            this.subRadio.TabIndex = 1;
            this.subRadio.TabStop = true;
            this.subRadio.Text = "-";
            this.subRadio.UseVisualStyleBackColor = true;
            this.subRadio.CheckedChanged += new System.EventHandler(this.subRadio_CheckedChanged);
            // 
            // addRadio
            // 
            this.addRadio.AutoSize = true;
            this.addRadio.Location = new System.Drawing.Point(35, 41);
            this.addRadio.Name = "addRadio";
            this.addRadio.Size = new System.Drawing.Size(31, 17);
            this.addRadio.TabIndex = 0;
            this.addRadio.TabStop = true;
            this.addRadio.Text = "+";
            this.addRadio.UseVisualStyleBackColor = true;
            this.addRadio.CheckedChanged += new System.EventHandler(this.addRadio_CheckedChanged);
            // 
            // txtFirst
            // 
            this.txtFirst.Location = new System.Drawing.Point(15, 96);
            this.txtFirst.Name = "txtFirst";
            this.txtFirst.Size = new System.Drawing.Size(73, 20);
            this.txtFirst.TabIndex = 1;
            // 
            // lblFirst
            // 
            this.lblFirst.AutoSize = true;
            this.lblFirst.Location = new System.Drawing.Point(36, 80);
            this.lblFirst.Name = "lblFirst";
            this.lblFirst.Size = new System.Drawing.Size(26, 13);
            this.lblFirst.TabIndex = 2;
            this.lblFirst.Text = "First";
            // 
            // lblSecond
            // 
            this.lblSecond.AutoSize = true;
            this.lblSecond.Location = new System.Drawing.Point(230, 80);
            this.lblSecond.Name = "lblSecond";
            this.lblSecond.Size = new System.Drawing.Size(44, 13);
            this.lblSecond.TabIndex = 4;
            this.lblSecond.Text = "Second";
            // 
            // txtSecond
            // 
            this.txtSecond.Location = new System.Drawing.Point(215, 96);
            this.txtSecond.Name = "txtSecond";
            this.txtSecond.Size = new System.Drawing.Size(73, 20);
            this.txtSecond.TabIndex = 3;
            this.txtSecond.TextChanged += new System.EventHandler(this.txtSecond_TextChanged);
            // 
            // lblEquals
            // 
            this.lblEquals.AutoSize = true;
            this.lblEquals.Location = new System.Drawing.Point(314, 99);
            this.lblEquals.Name = "lblEquals";
            this.lblEquals.Size = new System.Drawing.Size(13, 13);
            this.lblEquals.TabIndex = 5;
            this.lblEquals.Text = "=";
            // 
            // lblResult
            // 
            this.lblResult.AutoSize = true;
            this.lblResult.Location = new System.Drawing.Point(369, 80);
            this.lblResult.Name = "lblResult";
            this.lblResult.Size = new System.Drawing.Size(37, 13);
            this.lblResult.TabIndex = 7;
            this.lblResult.Text = "Result";
            // 
            // txtResult
            // 
            this.txtResult.BackColor = System.Drawing.SystemColors.Window;
            this.txtResult.Location = new System.Drawing.Point(354, 96);
            this.txtResult.Name = "txtResult";
            this.txtResult.ReadOnly = true;
            this.txtResult.Size = new System.Drawing.Size(73, 20);
            this.txtResult.TabIndex = 6;
            this.txtResult.Text = "NA";
            // 
            // bttnCalculate
            // 
            this.bttnCalculate.Location = new System.Drawing.Point(264, 157);
            this.bttnCalculate.Name = "bttnCalculate";
            this.bttnCalculate.Size = new System.Drawing.Size(124, 23);
            this.bttnCalculate.TabIndex = 8;
            this.bttnCalculate.Text = "Perform Operation";
            this.bttnCalculate.UseVisualStyleBackColor = true;
            this.bttnCalculate.Click += new System.EventHandler(this.bttnCalculate_Clicked);
            // 
            // Window
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(444, 211);
            this.Controls.Add(this.bttnCalculate);
            this.Controls.Add(this.lblResult);
            this.Controls.Add(this.txtResult);
            this.Controls.Add(this.lblEquals);
            this.Controls.Add(this.lblSecond);
            this.Controls.Add(this.txtSecond);
            this.Controls.Add(this.lblFirst);
            this.Controls.Add(this.txtFirst);
            this.Controls.Add(this.grpOperations);
            this.Name = "Window";
            this.Text = "Calculator By: Julian";
            this.grpOperations.ResumeLayout(false);
            this.grpOperations.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.GroupBox grpOperations;
        private System.Windows.Forms.RadioButton addRadio;
        private System.Windows.Forms.TextBox txtFirst;
        private System.Windows.Forms.Label lblFirst;
        private System.Windows.Forms.RadioButton subRadio;
        private System.Windows.Forms.RadioButton divRadio;
        private System.Windows.Forms.RadioButton multRadio;
        private System.Windows.Forms.Label lblSecond;
        private System.Windows.Forms.TextBox txtSecond;
        private System.Windows.Forms.Label lblEquals;
        private System.Windows.Forms.Label lblResult;
        private System.Windows.Forms.TextBox txtResult;
        private System.Windows.Forms.Button bttnCalculate;
    }
}

