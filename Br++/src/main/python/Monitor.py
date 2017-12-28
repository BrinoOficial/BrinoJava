import serial
import threading
import getch
import sys

arg = sys.argv

def serialListener():
    while 1:
        while s.inWaiting()+1 :
            print(s.read().decode(), end='',flush=True)

listenerThread = threading.Thread(target=serialListener)

if __name__ == '__main__':
    s=serial.Serial(arg[1], timeout=0)
    print(s.name)
    listenerThread.start()
    while 1:
        char = getch.getch()
        s.write(bytes(char, 'UTF-8'))
