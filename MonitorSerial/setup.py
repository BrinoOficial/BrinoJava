#setup.py
from cx_Freeze import setup, Executable
setup(
    name = "MonitorSerial",
    version = "1.0",
    options = {"build_exe":{
        'packages':["os","sys","ctypes","win64con"],
        'include_msvcr':True,
    }},
    executables = [Executable("MonitorSerial.py", base = "Win64GUI")]
    )
