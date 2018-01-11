import time
import hmac
import urllib
import requests
import hashlib
import base64
import sys
import json
import pdb

API_KEY = ''
API_SECRET = ''


def Cryptopia_query( method, req = None ):
    if not req:
         req = {}
    time.sleep( 1 )
    public_set = set([ 'GetCurrencies', 'GetTradePairs', 'GetMarkets', 'GetMarket', 'GetMarketHistory', 'GetMarketOrders' ])
    private_set = set([ 'GetBalance', 'GetDepositAddress', 'GetOpenOrders', 'GetTradeHistory', 'GetTransactions', 'SubmitTrade', 'CancelTrade', 'SubmitTip' ])
    if method in public_set:
         url = 'https://www.cryptopia.co.nz/api/' + method
         if req:
             for param in req:
                 url += '/' + str( param )
         response = requests.get( url )
    elif method in private_set:
         url = 'https://www.cryptopia.co.nz/Api/' + method
         nonce = str( int( time.time() ) )
         post_data = json.dumps( req );
         m = hashlib.md5()
         m.update(post_data.encode('utf-8'))
         requestContentBase64String = base64.b64encode(m.digest()).decode('utf-8')
         signature = API_KEY + 'POST' + urllib.parse.quote_plus( url ).lower() + nonce + requestContentBase64String
         hmacsignature = base64.b64encode(hmac.new(base64.b64decode( API_SECRET ), signature.encode('utf-8'), hashlib.sha256).digest())
         header_value = 'amx ' + API_KEY + ':' + hmacsignature.decode() + ':' + nonce
         headers = { 'Authorization': header_value, 'Content-Type':'application/json; charset=utf-8' }
         response = requests.post( url, data = post_data, headers = headers )
    return response.json()