using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

//Julian Sellanes (301494667)

namespace Lab19
{
    public partial class Window : Form
    {
        public Window()
        {
            InitializeComponent();
        }

        private void txtFirst_TextChanged(object sender, EventArgs e)
        {

        }

        private void txtSecond_TextChanged(object sender, EventArgs e)
        {

        }

        private void addRadio_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void subRadio_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void multRadio_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void divRadio_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void bttnCalculate_Clicked(object sender, EventArgs e)
        {
            if (!double.TryParse(txtFirst.Text, out double firstNum) || !double.TryParse(txtSecond.Text, out double secondNum))
            {
                MessageBox.Show("Enter valid numbers.", "Input Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            double result = 0;

            if (addRadio.Checked)
                result = firstNum + secondNum;
            else if (subRadio.Checked)
                result = firstNum - secondNum;
            else if (multRadio.Checked)
                result = firstNum * secondNum;
            else if (divRadio.Checked)
            {
                if (secondNum == 0)
                {
                    MessageBox.Show("Cannot divide by zero.", "Math Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                result = firstNum / secondNum;
            }

            txtResult.Text = result.ToString();
        }
    }
}
