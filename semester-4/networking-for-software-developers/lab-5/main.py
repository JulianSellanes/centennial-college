import tkinter as tk
from tkinter import ttk, messagebox

# Julian Sellanes (301494667)

class StudentSurveyApp(tk.Tk):
    def __init__(self):
        super().__init__()

        # Window
        self.title("Centennial College")
        self.minsize(640, 360)
        self.configure(bg="#d9d9d9")

        # Variables
        self.full_name_var = tk.StringVar()
        self.residency_var = tk.StringVar()
        self.program_var = tk.StringVar()

        # Checkbuttons
        self.course_prog1_var = tk.StringVar()
        self.course_web_var = tk.StringVar()
        self.course_softeng_var = tk.StringVar()

        # Main container frame
        self.container = tk.Frame(
            self,
            bg="#7BC67B",
            bd=3,
            relief="ridge",
            padx=22,
            pady=18
        )
        self.container.grid(row=0, column=0, sticky="nsew", padx=22, pady=18)

        self.grid_rowconfigure(0, weight=1)
        self.grid_columnconfigure(0, weight=1)

        self.container.grid_columnconfigure(0, weight=0)
        self.container.grid_columnconfigure(1, weight=1)
        self.container.grid_rowconfigure(5, weight=1)

        self.style = ttk.Style(self)
        self.style.theme_use("clam")
        self.style.configure(
            "Survey.TCombobox",
            fieldbackground="white",
            background="white",
            foreground="black"
        )
        self.style.map(
            "Survey.TCombobox",
            fieldbackground=[("readonly", "white")],
            foreground=[("readonly", "black")],
            selectbackground=[("readonly", "white")],
            selectforeground=[("readonly", "black")]
        )
        self.option_add("*TCombobox*Listbox.background", "white")
        self.option_add("*TCombobox*Listbox.foreground", "black")

        self._build_widgets()
        self.reset_form()

    def _build_widgets(self):
        # Header
        self.lbl_title = tk.Label(
            self.container,
            text="ICET Student Survey",
            bg="#7BC67B",
            fg="black",
            font=("Arial", 24, "bold italic")
        )
        self.lbl_title.grid(row=0, column=0, columnspan=2, pady=(0, 14), sticky="n")

        # Full name
        self.lbl_name = tk.Label(self.container, text="Full name:", bg="#7BC67B", font=("Arial", 13), fg="black")
        self.lbl_name.grid(row=1, column=0, sticky="e", padx=(0, 14), pady=6)

        self.ent_name = tk.Entry(self.container, textvariable=self.full_name_var, font=("Arial", 12), bg="white", fg="black", insertbackground="black")
        self.ent_name.grid(row=1, column=1, sticky="ew", pady=6)

        # Residency
        self.lbl_res = tk.Label(self.container, text="Residency:", bg="#7BC67B", font=("Arial", 13), fg="black")
        self.lbl_res.grid(row=2, column=0, sticky="ne", padx=(0, 14), pady=6)

        self.res_frame = tk.Frame(self.container, bg="#7BC67B")
        self.res_frame.grid(row=2, column=1, sticky="w", pady=6)

        self.rb_dom = tk.Radiobutton(
            self.res_frame,
            text="Domestic",
            variable=self.residency_var,
            value="dom",
            bg="#7BC67B",
            activebackground="#7BC67B",
            font=("Arial", 12),
            fg="black"
        )
        self.rb_dom.grid(row=0, column=0, sticky="w", pady=(0, 8))

        self.rb_intl = tk.Radiobutton(
            self.res_frame,
            text="International",
            variable=self.residency_var,
            value="intl",
            bg="#7BC67B",
            activebackground="#7BC67B",
            font=("Arial", 12),
            fg="black"
        )
        self.rb_intl.grid(row=1, column=0, sticky="w")

        # Program
        self.lbl_prog = tk.Label(self.container, text="Program:", bg="#7BC67B", font=("Arial", 13), fg="black")
        self.lbl_prog.grid(row=3, column=0, sticky="e", padx=(0, 14), pady=6)

        self.cbo_program = ttk.Combobox(
            self.container,
            textvariable=self.program_var,
            values=["AI", "Gaming", "Health", "Software"],
            state="readonly",
            style="Survey.TCombobox"
        )
        self.cbo_program.grid(row=3, column=1, sticky="ew", pady=6)

        # Courses
        self.lbl_courses = tk.Label(self.container, text="Courses:", bg="#7BC67B", font=("Arial", 13), fg="black")
        self.lbl_courses.grid(row=4, column=0, sticky="ne", padx=(0, 14), pady=6)

        self.courses_frame = tk.Frame(self.container, bg="#7BC67B")
        self.courses_frame.grid(row=4, column=1, sticky="w", pady=6)

        self.chk_prog1 = tk.Checkbutton(
            self.courses_frame,
            text="Programming I",
            variable=self.course_prog1_var,
            onvalue="COMP100",
            offvalue="",
            bg="#7BC67B",
            activebackground="#7BC67B",
            font=("Arial", 12),
            fg="black"
        )
        self.chk_prog1.grid(row=0, column=0, sticky="w", pady=(0, 8))

        self.chk_web = tk.Checkbutton(
            self.courses_frame,
            text="Web Page Design",
            variable=self.course_web_var,
            onvalue="COMP213",
            offvalue="",
            bg="#7BC67B",
            activebackground="#7BC67B",
            font=("Arial", 12),
            fg="black"
        )
        self.chk_web.grid(row=1, column=0, sticky="w", pady=(0, 8))

        self.chk_softeng = tk.Checkbutton(
            self.courses_frame,
            text="Software Engineering",
            variable=self.course_softeng_var,
            onvalue="COMP120",
            offvalue="",
            bg="#7BC67B",
            activebackground="#7BC67B",
            font=("Arial", 12),
            fg="black"
        )
        self.chk_softeng.grid(row=2, column=0, sticky="w")

        # Buttons
        self.btn_frame = tk.Frame(self.container, bg="#7BC67B")
        self.btn_frame.grid(row=6, column=0, columnspan=2, sticky="ew", pady=(18, 0))
        self.btn_frame.grid_columnconfigure(0, weight=1)
        self.btn_frame.grid_columnconfigure(1, weight=1)
        self.btn_frame.grid_columnconfigure(2, weight=1)

        self.btn_reset = tk.Button(self.btn_frame, text="Reset", command=self.reset_form, font=("Arial", 12), bg="#7BC67B", activebackground="#7BC67B", fg="black", activeforeground="black", bd=0, relief="flat", highlightthickness=0, highlightbackground="#7BC67B")
        self.btn_reset.grid(row=0, column=0, sticky="ew", padx=(0, 12), ipady=6)

        self.btn_ok = tk.Button(self.btn_frame, text="Ok", command=self.show_info, font=("Arial", 12),  bg="#7BC67B", activebackground="#7BC67B", fg="black", activeforeground="black", bd=0, relief="flat", highlightthickness=0, highlightbackground="#7BC67B")
        self.btn_ok.grid(row=0, column=1, sticky="ew", padx=12, ipady=6)

        self.btn_exit = tk.Button(self.btn_frame, text="Exit", command=self.exit_app, font=("Arial", 12),  bg="#7BC67B", activebackground="#7BC67B", fg="black", activeforeground="black", bd=0, relief="flat", highlightthickness=0, highlightbackground="#7BC67B")
        self.btn_exit.grid(row=0, column=2, sticky="ew", padx=(12, 0), ipady=6)

        self.bind("<Return>", lambda _e: self.show_info())

    def reset_form(self):
        self.full_name_var.set("Narendra Pershad")
        self.residency_var.set("dom")
        self.program_var.set("Health")

        self.course_prog1_var.set("COMP100")
        self.course_web_var.set("")
        self.course_softeng_var.set("")

        self.ent_name.focus_set()
        self.ent_name.selection_range(0, tk.END)

    def show_info(self):
        name = (self.full_name_var.get() or "").strip()
        program = (self.program_var.get() or "").strip()
        residency = (self.residency_var.get() or "").strip()

        courses = [
            self.course_prog1_var.get(),
            self.course_web_var.get(),
            self.course_softeng_var.get(),
        ]
        selected = [c for c in courses if c]

        course_text = f"({' '.join(selected)}{' ' if selected else ''})"

        msg = f"{name}\n{program}\n{residency}\n{course_text}"
        messagebox.showinfo("Information", msg)

    def exit_app(self):
        self.destroy()


if __name__ == "__main__":
    app = StudentSurveyApp()
    app.mainloop()