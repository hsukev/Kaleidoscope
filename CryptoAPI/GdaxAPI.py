import time as timer
import requests
import sys
import csv
import pdb
from io import StringIO
from datetime import datetime, timedelta

def BitcoinPrice_query(time =  None):
    lastPrice = 0
    if time is not None:
        if '.' in time:
            time, junkNotUsed = time.split('.')
        
        datetimeObj = datetime.strptime(time, '%Y-%m-%dT%H:%M:%S') 
        utc_endtime = datetimeObj - timedelta(minutes=45)
        utc_starttime = datetimeObj - timedelta(minutes=60)
        endtime = utc_endtime.strftime('%Y-%m-%dT%H:%M:%S') + '.00000Z'
        starttime = utc_starttime.strftime('%Y-%m-%dT%H:%M:%S') + '.00000Z'
        url = 'https://api.gdax.com/products/BTC-USD/candles?start=' + starttime + '&end=' + endtime + '&granularity=60'
        while True:
            response = requests.get(url)
            if response.status_code == 200:
                break
            else:
                timer.sleep(1)
                continue
        lastPrice = response.json()[0][4]
    else: 
        url = 'https://api.gdax.com/products/BTC-USD/ticker'
        while True:
            response = requests.get(url)
            if response.status_code == 200:
                break
            else:
                timer.sleep(1)
                continue
        lastPrice = response.json()['price']
    return float(lastPrice)