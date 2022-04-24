#!/usr/bin/env python

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522
import time
import os
from supabase import create_client, Client
from dotenv import dotenv_values
reader = SimpleMFRC522()
value = 0
config = dotenv_values(".env")
key: str =os.environ.get(" eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9xaHhja2htYWl0cGRkd3N0cGR6Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTY0NzQzMzM1NSwiZXhwIjoxOTYzMDA5MzU1fQ.qH4HF3yn4I4i3ZZKH22RXwmlHo5z6oNyS6XhdzbcuEo")
url: str =os.environ.get("https://oqhxckhmaitpddwstpdz.supabase.co")
supabase: Client = create_client(url,key)
while True:
 try:
         id, text = reader.read()
         print(id)
         print(text)
         value = text
         print(value)
         ts = time.strftime('%H:%M:%S')
         print(ts)
         async def insert_in() :
           error, result = await (
                 supabase.table("StudentData")
                 .update(
                 { 'studentnum' : value },
                 { 'inout': 'in' }
                 )
           )
         time.sleep(2)
 finally:
         GPIO.cleanup()

 if id == 1046211500003:

         text = input('New data:')
         print("Now place your tag to write")
         reader.write(text)
         print("Written")
         time.sleep(2)
