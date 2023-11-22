import tkinter as tk
import os
import io
import glob
import pandas as pd
import time
import json
import pathlib
import re
import subprocess

from tkinter import messagebox
from tkinter import ttk
from tkinter import filedialog
from tkinter import StringVar
from tkinter import IntVar
from tkinter import Toplevel


class MainApp(tk.Tk):

    def __init__(self, *args, **kwargs):
        tk.Tk.__init__(self, *args, **kwargs)
        tk.Tk.wm_title(self, "Defects4jGUI")
        container = tk.Frame(self)

        container.pack(side="top", fill="both", expand=True)

        proj = StringVar()
        ver = StringVar()

        self.frames = {}

        for F in (StartPage, Defects4jGUI):
            frame = F(container, self, proj, ver)

            self.frames[F] = frame

            frame.grid(row=0, column=0, sticky="nsew")

        self.show_frame(StartPage)

    def show_frame(self, cont):
        frame = self.frames[cont]
        frame.tkraise()


class StartPage(tk.Frame):
    def __init__(self, parent, controller, proj, ver):
        tk.Frame.__init__(self, parent)

        self.proj_var = StringVar()
        self.proj_pick = StringVar()
        self.ver_pick = StringVar()

        self.user_path = os.path.expanduser('~')

        self.projects = []
        self.startup_check()

        self.start_project = tk.LabelFrame(self, text="Create Project")
        self.start_project.grid(row=0, column=0, sticky="news", padx=20, pady=20)

        self.project_name_label = tk.Label(self.start_project, text="Project")
        self.project_name_label.grid(row=0, column=0)

        self.project_dropdown = ttk.Combobox(self.start_project, values=["Cli", "Gson", "Lang"],
                                             state="readonly", textvariable=self.proj_pick)
        self.project_dropdown.grid(row=0, column=1)

        self.project_version = tk.Label(self.start_project, text="Version")
        self.project_version.grid(row=1, column=0)

        self.version_dropdown = ttk.Combobox(self.start_project, state="readonly", textvariable=self.ver_pick)
        self.version_dropdown.grid(row=1, column=1)

        self.project_dropdown.bind('<<ComboboxSelected>>', lambda event: self.project_select())

        self.checkout_button = tk.Button(self.start_project, text="Checkout",
                                         command=lambda: [self.compile_button.config(state="normal"),
                                                          self.checkout_button.config(state="disabled"),
                                                          self.checkout(self.proj_pick, self.ver_pick)])
        self.checkout_button.grid(row=2, column=0)

        self.compile_button = tk.Button(self.start_project, text="Compile", state="disabled",
                                        command=lambda: [self.compile(self.proj_pick, self.ver_pick),
                                                         self.compile_button.config(state="disabled")])
        self.compile_button.grid(row=2, column=1)

        for widget in self.start_project.winfo_children():
            widget.grid_configure(padx=10, pady=5)

        self.load_project = tk.LabelFrame(self, text="Load Project")
        self.load_project.grid(row=1, column=0, sticky="news", padx=20, pady=20)

        self.project_label = tk.Label(self.load_project, text="Project")
        self.project_label.grid(row=0, column=0)

        self.load_dropdown = ttk.Combobox(self.load_project, values=self.projects,
                                          state="readonly", textvariable=self.proj_var)
        self.load_dropdown.grid(row=0, column=1)

        self.load_button = tk.Button(self.load_project, text="Load",
                                     command=lambda: [proj.set(self.proj_var.get()[:-4]),
                                                      ver.set(self.proj_var.get()[len(self.proj_var.get())-3:]),
                                                      controller.show_frame(Defects4jGUI)])
        self.load_button.grid(row=1, column=0)

        for widget in self.load_project.winfo_children():
            widget.grid_configure(padx=10, pady=5)

    def project_select(self):
        self.version_dropdown.set('')
        match self.proj_pick.get():
            case 'Cli':
                self.version_dropdown['values'] = ["32f"]
            case 'Gson':
                self.version_dropdown['values'] = ["15f"]
            case 'Lang':
                self.version_dropdown['values'] = ["53f"]
            case _:
                print("Invalid project selected.")

    def startup_check(self):
        path = pathlib.Path().resolve() / "data.json"
        if os.path.isfile(path) and os.access(path, os.R_OK):
            # checks if file exists
            print("File exists and is readable")
            f = open('data.json')
            data = json.load(f)

            for i in data:
                if "name" in i:
                    self.projects.append(i['name'])

            f.close()
        else:
            print("Either file is missing or is not readable, creating file...")
            with io.open(os.path.join(pathlib.Path().resolve(), 'data.json'), 'w') as db_file:
                db_file.write(json.dumps([]))

    def checkout(self, proj_pick, ver_pick):
        command = ("defects4j checkout -p " + proj_pick.get() + " -v" + ver_pick.get() + " -w $HOME/"
                   + proj_pick.get() + "-" + ver_pick.get())
        os.system(command)

    def compile(self, proj_pick, ver_pick):
        command = ("defects4j compile -w $HOME/" + proj_pick.get() + "-" + ver_pick.get())
        os.system(command)
        self.projects.append(proj_pick.get() + "-" + ver_pick.get())
        self.load_dropdown['values'] = self.projects

        with open("data.json") as fp:
            data = json.load(fp)

        data.append({
            "name": proj_pick.get() + "-" + ver_pick.get()
        })

        with open("data.json", 'w') as json_file:
            json.dump(data, json_file,
                      indent=4,
                      separators=(',', ': '))


class Defects4jGUI(tk.Frame):
    def __init__(self, parent, controller, proj, ver):
        tk.Frame.__init__(self, parent)

        self.csv_folder_path = StringVar()
        self.suite_folder_path = StringVar()
        self.tool_selected = StringVar()
        self.csv_filename = StringVar()
        self.student_check = IntVar()
        self.developer_check = IntVar()
        self.kill_matrix_check = IntVar()

        self.user_path = os.path.expanduser('~')

        # Project Management
        self.project_frame = tk.LabelFrame(self, text="Project Details")
        self.project_frame.grid(row=0, column=0, sticky="news", padx=20, pady=5)

        self.project_name_label = tk.Label(self.project_frame, text="Project: ")
        self.project_name_label.grid(row=0, column=0, padx=(0, 270), pady=5)

        self.project_name = tk.Label(self.project_frame, textvariable=proj)
        self.project_name.grid(row=0, column=0, padx=(0, 180))

        self.version_label = tk.Label(self.project_frame, text="Version: ")
        self.version_label.grid(row=1, column=0, padx=(0, 270), pady=5)

        self.version_name = tk.Label(self.project_frame, textvariable=ver)
        self.version_name.grid(row=1, column=0, padx=(0, 180))

        self.coverage_button = tk.Button(self.project_frame, text="Coverage", command=lambda: self.coverage(proj, ver))
        self.coverage_button.grid(row=2, column=0, padx=(0, 250), pady=5)

        self.coverage_label = tk.Label(self.project_frame, text="Get project coverage information.",
                                       justify="center")
        self.coverage_label.grid(row=2, column=0, padx=(140, 0), pady=5)

        # Mutation Tool Settings
        self.mutator_frame = tk.LabelFrame(self, text="Mutator Settings")
        self.mutator_frame.grid(row=2, column=0, sticky="news", padx=20, pady=5)

        self.mutation_tool_label = tk.Label(self.mutator_frame, text="Mutation Tool")
        self.mutation_tool_label.grid(row=0, column=0, padx=10, pady=5)

        self.mutation_tool_dropdown = ttk.Combobox(self.mutator_frame, values=["pit", "major", "judy"],
                                                   state="readonly", textvariable=self.tool_selected)
        self.mutation_tool_dropdown.grid(row=0, column=1, padx=10, pady=5)
        self.mutation_tool_dropdown.bind("<<ComboboxSelected>>", lambda event: self.tool_select_check())

        self.generate_button = tk.Button(self.mutator_frame, text="Generate",
                                         command=lambda: self.generate_mutants(proj, ver))
        self.generate_button.grid(row=1, column=0, padx=10, pady=5)

        self.student_test_check = tk.Checkbutton(self.mutator_frame, text="Include student test cases.", variable=self.student_check)
        self.student_test_check.grid(row=1, column=1, padx=10, pady=5)

        self.dev_test_check = tk.Checkbutton(self.mutator_frame, text="Exclude developer test cases.", variable=self.developer_check)
        self.dev_test_check.grid(row=2, column=1, padx=(15, 0), pady=5)

        self.pit_mutation_level_label = tk.Label(self.mutator_frame, text="Mutation Level")
        self.pit_mutation_level_dropdown = ttk.Combobox(self.mutator_frame, values=["Stronger", "Default", "All"],
                                                        state="readonly")
        self.pit_mutation_level_dropdown.current(1)
        self.pit_mutation_level_dropdown.configure(state="disabled")

        # Analyzer Settings
        self.analyzer_frame = tk.LabelFrame(self, text="Analyzer")
        self.analyzer_frame.grid(row=4, column=0, sticky="news", padx=20, pady=5)

        self.analyzer_label = tk.Label(self.analyzer_frame, text="Generate live mutant output file.")
        self.analyzer_label.grid(row=1, column=0, padx=(100, 0), pady=5)

        self.filename_label = tk.Label(self.analyzer_frame, text="Filename:")
        self.filename_label.grid(row=0, column=0, padx=(0, 300), pady=5)

        self.filename_entry = tk.Entry(self.analyzer_frame, textvariable=self.csv_filename)
        self.filename_entry.grid(row=0, column=0, padx=(50, 0), pady=5)

        self.analyze_button = tk.Button(self.analyzer_frame, text="Analyze",
                                        command=lambda: [self.analyze_report(proj, ver),
                                                         time.sleep(2),
                                                         self.data_overlay(proj, ver),
                                                         self.launch_kill_matrix(proj, ver)])
        self.analyze_button.grid(row=1, column=0, padx=(0, 290), pady=5)

        self.major_kill_matrix_check = tk.Checkbutton(self.analyzer_frame, text="Include mutant Kill Matrix.",
                                                      variable=self.kill_matrix_check)

        self.summary_label = tk.Label(self.analyzer_frame, text="Get mutant live/killed summary report.", justify="center")
        self.summary_label.grid(row=3, column=0, padx=(140, 0), pady=5)

        self.summary_button = tk.Button(self.analyzer_frame, text="Summary", command=lambda: self.summary(proj, ver))
        self.summary_button.grid(row=3, column=0, padx=(0, 280), pady=5)

        self.directory_frame_two = tk.LabelFrame(self)
        self.directory_frame_two.grid(row=3, column=0, sticky="news", padx=20, pady=5)

        self.csv_browse_label = tk.Label(self.directory_frame_two, text="Select path for output CSV file.", justify="center")
        self.csv_browse_label.grid(row=0, column=0)

        self.csv_path_label = tk.Entry(self.directory_frame_two, width=50, textvariable=self.csv_folder_path)
        self.csv_path_label.grid(row=1, column=0)

        self.csv_browse_button = tk.Button(self.directory_frame_two, text="Browse", command=self.csv_browse_button)
        self.csv_browse_button.grid(row=1, column=1)

        for widget in self.directory_frame_two.winfo_children():
            widget.grid_configure(padx=10, pady=5)

        self.directory_frame = tk.LabelFrame(self)
        self.directory_frame.grid(row=1, column=0, sticky="news", padx=20, pady=5)

        self.suite_browse_label = tk.Label(self.directory_frame, text="Select path to student test suite.", justify="center")
        self.suite_browse_label.grid(row=2, column=0)

        self.suite_path_label = tk.Entry(self.directory_frame, width=50, textvariable=self.suite_folder_path)
        self.suite_path_label.grid(row=3, column=0)

        self.suite_browse_button = tk.Button(self.directory_frame, text="Browse", command=self.suite_browse_button)
        self.suite_browse_button.grid(row=3, column=1)

        for widget in self.directory_frame.winfo_children():
            widget.grid_configure(padx=10, pady=5)

    def generate_mutants(self, proj, ver):

        path = self.user_path + "/" + proj.get() + "-" + ver.get() + "/tools_output/" + self.tool_selected.get() + "/*"
        files = glob.glob(path)

        for f in files:
            os.remove(f)

        student_test = ""
        developer_test = "--all-dev "

        if self.student_check.get() == 1:
            student_test = "-t " + self.suite_folder_path.get() + "/StudentTest.java "

        if self.developer_check.get() == 1:
            developer_test = ""

        command = ("python3 analyzer.py run $HOME/" + proj.get() + "-" + ver.get() + " "
                   + developer_test + student_test + "--tools " + self.tool_selected.get())
        os.system(command)

    def analyze_report(self, proj, ver):

        version = ver.get()[:-1]
        projectname = proj.get() + "-" + ver.get()
        tool = self.tool_selected.get()

        match tool:
            case "pit":
                dir_path = self.user_path + '/' + projectname + '/tools_output/pit/'
                filetype = "*.xml"
                filename = ""
                for file_path in os.listdir(dir_path):
                    if file_path.endswith(filetype[1:]):
                        filename = file_path
                command = ("python3 reportsanalyzer.py table -p " + proj.get() + " -b "
                           + version + " -t " + self.tool_selected.get()
                           + " $HOME/" + projectname + "/tools_output/pit/" + filename
                           + " -o " + self.csv_folder_path.get() + "/" + self.csv_filename.get() + ".csv")
                os.system(command)
            case "judy":
                dir_path = self.user_path + '/' + projectname + '/tools_output/judy/'
                filetype = "*.json"
                filename = ""
                for file_path in os.listdir(dir_path):
                    if file_path.endswith(filetype[1:]):
                        filename = file_path
                command = ("python3 reportsanalyzer.py table -p " + proj.get() + " -b "
                           + version + " -t " + self.tool_selected.get()
                           + " $HOME/" + projectname + "/tools_output/judy/" + filename
                           + " -o " + self.csv_folder_path.get() + "/" + self.csv_filename.get() + ".csv")
                os.system(command)
            case "major":
                command = ("python3 reportsanalyzer.py table -p " + proj.get() + " -b "
                           + version + " -t " + self.tool_selected.get()
                           + " $HOME/" + projectname + "/tools_output/major/ -o "
                           + self.csv_folder_path.get() + "/" + self.csv_filename.get() + ".csv")
                os.system(command)
            case _:
                print("No tool selection was found.")

    def summary(self, proj, ver):

        version = ver.get()[:-1]
        projectname = proj.get() + "-" + ver.get()
        tool = self.tool_selected.get()
        filename = ""

        if tool == "pit":
            dir_path = self.user_path + '/' + projectname + '/tools_output/pit/'
            filetype = "*.xml"
            for file_path in os.listdir(dir_path):
                if file_path.endswith(filetype[1:]):
                    filename = "/" + file_path
        elif tool == "judy":
            dir_path = self.user_path + '/' + projectname + '/tools_output/judy/'
            filetype = "*.json"
            for file_path in os.listdir(dir_path):
                if file_path.endswith(filetype[1:]):
                    filename = "/" + file_path

        command = ("python3 reportsanalyzer.py summary -p " + proj.get()
                   + " -b " + version + " -t " + tool + " $HOME/" + projectname + "/tools_output/" + tool + filename)
        os.system(command)

    def coverage(self, proj, ver):
        command = "defects4j coverage -w $HOME/" + proj.get() + "-" + ver.get()
        os.system(command)

    def tool_select_check(self):
        if self.tool_selected.get() == "pit":
            self.pit_mutation_level_label.grid(row=3, column=0, padx=10, pady=5)
            self.pit_mutation_level_dropdown.grid(row=3, column=1, padx=10, pady=5)
            self.pit_mutation_level_dropdown.set("Default")
        else:
            self.pit_mutation_level_label.grid_remove()
            self.pit_mutation_level_dropdown.grid_remove()

        if self.tool_selected.get() == "major":
            self.major_kill_matrix_check.grid(row=2, column=0, padx=(50, 0), pady=5)
        else:
            self.kill_matrix_check.set(0)
            self.major_kill_matrix_check.grid_remove()

    def csv_browse_button(self):
        filename = filedialog.askdirectory()
        self.csv_folder_path.set(filename)

    def suite_browse_button(self):
        filename = filedialog.askdirectory()
        self.suite_folder_path.set(filename)

    def load_csv(self):
        path = self.csv_folder_path.get() + "/" + self.csv_filename.get() + ".csv"

        df = pd.read_csv(path)
        return df

    def pit_parse(self, df):
        mutant_list = df["Mutant"].tolist()

        json_list = df.iloc[:, 1].tolist()

        line_list = list()

        for i, j in enumerate(json_list):
            j = j.replace("\'", "\"")
            content = json.loads(j)
            line_list.insert(i, content["line"])

        operator_list = list()

        for i, j in enumerate(json_list):
            j = j.replace("\'", "\"")
            content = json.loads(j)
            operator_list.insert(i, content["mutator"][47:])

        class_list = list()

        for i, j in enumerate(json_list):
            j = j.replace("\'", "\"")
            content = json.loads(j)
            class_list.insert(i, content["mutated_class"])

        method_list = list()

        for i, j in enumerate(json_list):
            j = j.replace("\'", "\"")
            content = json.loads(j)
            method_list.insert(i, content["mutated_method"])

        sheet_data = list()

        for item1, item2, item3, item4, item5 in zip(mutant_list, line_list, operator_list, class_list, method_list):
            sheet_data.append((item1, item2, item3, item4, item5))

        return sheet_data

    def major_parse(self, df, proj, ver):
        mutant_list = df["Mutant"].tolist()

        projectname = proj.get() + "-" + ver.get()

        dir_path = self.user_path + '/' + projectname + '/tools_output/major/'
        filetype = "*.log"
        filename = ""
        for file_path in os.listdir(dir_path):
            if file_path.endswith(filetype[1:]):
                filename = file_path

        path = self.user_path + '/' + projectname + "/tools_output/major/" + filename

        with open(path) as f:
            f = f.readlines()

        line_list = list()
        operator_list = list()
        original_list = list()
        mutated_list = list()

        for line in f:
            attributes = re.split(":", line)
            line_list.append(attributes[5])
            operator_list.append(attributes[1])
            original_list.append(attributes[2])
            mutated_list.append(attributes[3])

        sheet_data = list()

        for item1, item2, item3, item4, item5 in zip(mutant_list, line_list, operator_list, original_list,
                                                     mutated_list):
            sheet_data.append((item1, item2, item3, item4, item5))

        return sheet_data

    def judy_parse(self, df):
        mutant_list = df["Mutant"].tolist()

        json_list = df.iloc[:, 1].tolist()

        line_list = list()

        for i, j in enumerate(json_list):
            j = j.replace("\'", "\"")
            content = json.loads(j)
            line_list.insert(i, content["line"])

        operator_list = list()

        for i, j in enumerate(json_list):
            j = j.replace("\'", "\"")
            content = json.loads(j)
            operator_list.insert(i, content["operator"])

        counter_list = list()

        for i, j in enumerate(json_list):
            j = j.replace("\'", "\"")
            content = json.loads(j)
            counter_list.insert(i, content["counter"])

        sheet_data = list()

        for item1, item2, item3, item4 in zip(mutant_list, line_list, operator_list, counter_list):
            sheet_data.append((item1, item2, item3, item4))

        return sheet_data

    def data_overlay(self, proj, ver):
        win = Toplevel()
        win.title("Live Mutant Data")

        data_frame = tk.Frame(win)
        data_frame.grid(row=0, column=0, pady=10)
        frame_scroll = ttk.Scrollbar(data_frame)
        frame_scroll.pack(side="right", fill="y")

        df = self.load_csv()

        tool = self.tool_selected.get()
        sheet_data = list()

        match tool:
            case "pit":
                cols = ("Mutant", "Line", "Operator", "Class", "Method")
                data_view = ttk.Treeview(data_frame, show="headings", columns=cols,
                                         yscrollcommand=frame_scroll.set, height=13)
                data_view.column("Mutant", width=100)
                data_view.column("Line", width=50)
                data_view.column("Operator", width=300)
                data_view.column("Class", width=300)
                data_view.column("Method", width=150)
                data_view.pack()
                frame_scroll.config(command=data_view.yview)
                for col_name in cols:
                    data_view.heading(col_name, text=col_name)
                sheet_data = self.pit_parse(df)
            case "major":
                cols = ("Mutant", "Line", "Operator", "Original", "Mutated")
                data_view = ttk.Treeview(data_frame, show="headings", columns=cols,
                                         yscrollcommand=frame_scroll.set, height=13)
                data_view.column("Mutant", width=100)
                data_view.column("Line", width=50)
                data_view.column("Operator", width=80)
                data_view.column("Original", width=300)
                data_view.column("Mutated", width=300)
                data_view.pack()
                frame_scroll.config(command=data_view.yview)
                for col_name in cols:
                    data_view.heading(col_name, text=col_name)
                sheet_data = self.major_parse(df, proj, ver)
            case "judy":
                cols = ("Mutant", "Line", "Operator", "Counter")
                data_view = ttk.Treeview(data_frame, show="headings", columns=cols,
                                         yscrollcommand=frame_scroll.set, height=13)
                data_view.column("Mutant", width=100)
                data_view.column("Line", width=50)
                data_view.column("Operator", width=80)
                data_view.column("Counter", width=80)
                data_view.pack()
                frame_scroll.config(command=data_view.yview)
                for col_name in cols:
                    data_view.heading(col_name, text=col_name)
                sheet_data = self.judy_parse(df)
            case _:
                print("No tool selection was found.")

        for value in sheet_data:
            if str(value[1]) == '0' or str(value[1]) == '-1':
                data_view.insert("", tk.END, values=value, tags=('ignore',))
            else:
                data_view.insert("", tk.END, values=value)

        data_view.tag_configure('ignore', background='grey')

    def launch_kill_matrix(self, proj, ver):
        if self.kill_matrix_check == 1:
            self.matrix_overlay(proj, ver)
        else:
            return

    def matrix_overlay(self, proj, ver):
        win = Toplevel()
        win.title("Kill Matrix")

        data_frame = tk.Frame(win)
        data_frame.grid(row=0, column=0, pady=10)
        frame_scroll = ttk.Scrollbar(data_frame)
        frame_scroll.pack(side="right", fill="y")

        path = self.user_path + "/" + proj.get() + "-" + ver.get() + "/killMap.csv"
        df = pd.read_csv(path)

        content_list = list()

        cols = ("TestNo", "MutantNo", "[FAIL | TIME | EXC]")
        data_view = ttk.Treeview(data_frame, show="headings", columns=cols,
                                 yscrollcommand=frame_scroll.set, height=13)
        data_view.column("TestNo", width=80)
        data_view.column("MutantNo", width=100)
        data_view.column("[FAIL | TIME | EXC]", width=200)
        data_view.pack()
        frame_scroll.config(command=data_view.yview)
        for col_name in cols:
            data_view.heading(col_name, text=col_name)
        content_list = df.values.tolist()

        for value in content_list:
            data_view.insert("", tk.END, values=value)


app = MainApp()
app.mainloop()
