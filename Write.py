#!/usr/bin/env python

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522
import time
import pymysql.cursors
from datetime import datetime


reader = SimpleMFRC522()
value = 0
if id == 1046211500003:

         text = input('New data:')
         print("Now place your tag to write")
         reader.write(text)
         print("Written")
         time.sleep(2)
else:
    def connect():
       conn = pymysql.connect(host='192.168.31.254',
                             user='pi',
                              password='raspberry1',
                              db='studentdatabase',
                              charset='utf8mb4',
                              cursorclass=pymysql.cursors.DictCursor)

       return conn
    while True:
      try:
           id, text = reader.read()
           print(id)
           print(text)
           value = text
           print(value)
           ts = time.strftime('%H:%M:%S')
           print(ts)
           time.sleep(2)

           d_time = datetime.today().strftime('%Y-%m-%d')
           t_long_time = datetime.now().strftime('%H:%M:%S')

           conn = connect()

           with conn.cursor() as cursor:
             cursor.execute("UPDATE STUDENT SET time= '%s', inorout= 'in' where studentnum ='%s'"),(t_long_time,value)
             conn.commit()
             print("Uploaded")
      finally:
           GPIO.cleanup()

