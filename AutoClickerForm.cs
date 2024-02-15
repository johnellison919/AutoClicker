using System.Diagnostics;
using System.Runtime.InteropServices;
using System.Windows.Forms;

namespace AutoClicker
{
    public partial class AutoClickerForm : Form
    {
        [DllImport("user32.dll")]
        private static extern void mouse_event(int dwFlags, int dx, int dy, int dwdata, int dwextrainfo);
       
        private bool autoClickEnabled = false;
        private KeyHandler globalHotKeyF1;
        private KeyHandler globalHotKeyF2;
        private KeyHandler globalHotKeyF6;
        private KeyHandler globalHotKeyF7;

        public AutoClickerForm()
        {
            globalHotKeyF1 = new KeyHandler(Keys.F1, this);
            globalHotKeyF1.Register();

            globalHotKeyF2 = new KeyHandler(Keys.F2, this);
            globalHotKeyF2.Register();

            globalHotKeyF6 = new KeyHandler(Keys.F6, this);
            globalHotKeyF6.Register();

            globalHotKeyF7 = new KeyHandler(Keys.F7, this);
            globalHotKeyF7.Register();

            InitializeComponent();
        }

        private enum MouseEventFlags
        {
            LeftDown = 2,
            LeftUp = 4,
        }

        private void PerformLeftClick(Point point)
        {
            mouse_event((int)MouseEventFlags.LeftDown, point.X, point.Y, 0, 0);
            mouse_event((int)MouseEventFlags.LeftUp, point.X, point.Y, 0, 0);
        }

        private void StartAutoClick()
        {
            if (!autoClickEnabled)
            {
                int interval = ((int)numericUpDown1.Value * 1000) + (int)numericUpDown2.Value;

                if (interval > 0)
                {
                    autoClickEnabled = true;


                    timer1.Interval = interval;
                    timer1.Enabled = true;
                    timer1.Start();
                }
            }
        }

        private void StopAutoClick()
        {
            if (autoClickEnabled)
            {
                autoClickEnabled = false;
                timer1.Stop();
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            StartAutoClick();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            StopAutoClick();
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            Point currentMousePosition = Cursor.Position;
            PerformLeftClick(currentMousePosition);
        }

        protected override void WndProc(ref Message m)
        {
            if (m.Msg == Constants.WM_HOTKEY_MSG_ID)
            {
                if (m.WParam == globalHotKeyF1.GetHashCode() || m.WParam == globalHotKeyF6.GetHashCode())
                {
                    StartAutoClick();
                }

                if (m.WParam == globalHotKeyF2.GetHashCode() || m.WParam == globalHotKeyF7.GetHashCode())
                {
                    StopAutoClick();
                }
            }

            base.WndProc(ref m);
        }
    }
}
