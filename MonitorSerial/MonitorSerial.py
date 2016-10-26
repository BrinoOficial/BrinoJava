# Monitor Serial Brino
# Criado por Mateus Berado
# em 26/10/2016
# versao 0.1
'''
 versao Python 3.5.1
 versao tkinter 8.6
 
 Esse codigo e distribuido sob uma licenca open source MIT License
 Copyright (c) 2016 Br.ino
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 IN THE SOFTWARE.
'''
import threading
import serial
from tkinter import *

f = open('./brino.pref','r')
pref = f.read()
f.close()
ca = pref.split("\n")
#port = filter(lambda x:'serial.port=' in x, ca)
needed_prefs = ['serial.port', 'serial.databits','serial.stopbits', 'serial.parity','serial.debug_rate']
prefs = [s for s in ca if any(xs in s for xs in needed_prefs)]
port = prefs[0].split('=')
baud_rate = prefs[4].split('=')

try:
    ser = serial.Serial(port[1], baud_rate[1])
except serial.serialutil.SerialException:
    print ("Arduino not connected")

class App(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        self.start()

    def callback(self):
        self.root.quit()
        ser.close()

    def run(self):
        self.root = Tk()
        self.root.protocol("WM_DELETE_WINDOW", self.callback)

        self.fr1 = Frame(self.root)
        self.fr1.pack()

        self.fr2 = Frame(self.root)
        self.fr2.pack(side = TOP, fill = X)

        self.botao = Button(self.fr2)
        self.botao['text'] = 'Enviar'
        #clique com o botao esquerdo
        self.botao.bind("<1>", self.enviar)
        self.botao.pack(side = RIGHT)

        self.envio = Entry(self.fr2)
        self.envio.focus_force()
        self.envio.bind("<Return>", self.enviar)
        self.envio.pack(side = LEFT, fill = BOTH, expand = True)

        self.texto = Text(self.root)
        self.texto.pack(side = BOTTOM)
        
        self.root.title('Monitor Serial')
        self.root.mainloop()
        
    def enviar(self, event):
        '''
        evento para enviar
        '''
        try:
            ser.write(self.envio.get().encode())
            self.envio.delete(0, 'end')
        except UnicodeEncodeError:
            print("Ooops")

app = App()
print('Now we can continue running code while mainloop runs!')

while True:
    try:
        if ser.inWaiting() > 0:
            try:
                app.texto.insert(END, ser.readline().decode('utf-8'))
            except UnicodeDecodeError:
                print("Ooops")
    except serial.serialutil.SerialException:
        break
        
