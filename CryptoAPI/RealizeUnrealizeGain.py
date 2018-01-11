from CryptopiaAPI import Cryptopia_query
from GdaxAPI import BitcoinPrice_query
import json
import pdb

CurrentBTCUSDPrice = BitcoinPrice_query()
# Make Public API call to get all BTC current market price
CurrentMarketsData = Cryptopia_query('GetMarkets')['Data']

def main():
    # Private
    trade_response = Cryptopia_query('GetTradeHistory', {'Count':'500'})
    balance_response = Cryptopia_query('GetBalance')
    balance_response_data = balance_response['Data']
    balance = list(filter(lambda x: x['Available']!=0.0, balance_response_data))
    trade_response_data = trade_response['Data']

    trade_dict = {}
    result = []
    
    for trade in trade_response_data:
        dict_data = {'Type':trade['Type'], 'Amount':trade['Amount'], 'Rate':trade['Rate'], 'Total':trade['Total'], 'Fee':trade['Fee'], 'TimeStamp':trade['TimeStamp']}
        if trade['Market'] not in trade_dict:
            trade_dict[trade['Market']] = [dict_data]
        else:
            trade_dict[trade['Market']].append(dict_data)

    # realized gain
    for key, value in trade_dict.items():
        print('Processing ' + key)
        cost = {'BTC': 0, 'USD': 0}
        currentValue = {'BTC': 0, 'USD': 0}
        realized_gain = {'BTC': 0, 'USD': 0}
        unrealized_gain= {'BTC': 0, 'USD': 0}
        
        value_sorted = sortByTime(value)
        buyList = list(filter(lambda x: x['Type']=='Buy', value_sorted))
        sellList = list(filter(lambda x: x['Type']=='Sell', value_sorted))
        totalBuyAmount = sum(item['Amount'] for item in buyList)
        totalSellAmount = sum(item['Amount'] for item in sellList)
        if totalBuyAmount > totalSellAmount:
            buyList, sellList, realized_gain = calcRealizedGainLoss(buyList, sellList)
            unrealized_gain, cost, currentValue = calcUnrealizedGainLoss(key, buyList)
        else:
            transactionFee = sum(item['Fee'] for item in sellList) + sum(item['Fee'] for item in buyList)
            realized_gain['BTC'] = sum(item['Total'] for item in sellList) - sum(item['Total'] for item in buyList) - transactionFee
            realized_gain['USD'] = realized_gain['BTC'] * CurrentBTCUSDPrice
            
        result.append({
            'Market':key,
            'BTC Realized Gain': realized_gain['BTC'],
            'BTC Unrealized Gain': unrealized_gain['BTC'],
            'BTC Cost': cost['BTC'],
            'BTC Current Value': currentValue['BTC'],
            'BTC Change': ((currentValue['BTC'] / cost['BTC'] if cost['BTC'] != 0 else 1) - 1) * 100,
            'USD Realized Gain': realized_gain['USD'],
            'USD Unrealized Gain': unrealized_gain['USD'],
            'USD Cost': cost['USD'],
            'USD Current Value': currentValue['USD'],
            'USD Change': ((currentValue['USD'] / cost['USD'] if cost['USD'] != 0 else 1) - 1) * 100
         })
         
        for balance_coin in balance:
            if balance_coin['Symbol'] + '/BTC' == key:
                balance.remove(balance_coin)
    for coin in balance:
        result.append({
            'Market':coin['Symbol'] + '/BTC',
            'BTC Realized Gain': 0,
            'BTC Unrealized Gain': 0,
            'BTC Cost': coin['Total'],
            'BTC Current Value': coin['Total'],
            'BTC Change': 0,
            'USD Realized Gain': 0,
            'USD Unrealized Gain': 0,
            'USD Cost': coin['Total'] * CurrentBTCUSDPrice,
            'USD Current Value': coin['Total'] * CurrentBTCUSDPrice,
            'USD Change': 0,
         })    
    result = sortByValue(result)
    for entry in result:
        printCoinStat(entry)
    printSummary(result)
def calcRealizedGainLoss(buyList, sellList):
    realized_gain = {'BTC': 0, 'USD': 0}
    transactionFeeBTC = 0
    transactionFeeUSD = 0
    
    while len(sellList) > 0:
        while len(buyList) > 0 and sellList[-1]['Amount'] > 0:
            amountBalance = sellList[-1]['Amount'] - buyList[-1]['Amount']
            if (amountBalance) < 0:
                realized_gain['BTC'] += (sellList[-1]['Amount'] * (sellList[-1]['Rate'] - buyList[-1]['Rate']))
                realized_gain['USD'] += realized_gain['BTC'] * BitcoinPrice_query(sellList[-1]['TimeStamp'])
                sellList[-1]['Amount'] = 0
                buyList[-1]['Amount'] = abs(amountBalance)
                
            elif (amountBalance) > 0:
                realized_gain['BTC'] += (buyList[-1]['Amount'] * (sellList[-1]['Rate'] - buyList[-1]['Rate']))
                realized_gain['USD'] += realized_gain['BTC'] * BitcoinPrice_query(buyList[-1]['TimeStamp'])
                sellList[-1]['Amount'] = abs(amountBalance)
                buyList[-1]['Amount'] = 0
            else:
                realized_gain['BTC'] += (buyList[-1]['Amount'] * (sellList[-1]['Rate'] - buyList[-1]['Rate']))
                realized_gain['USD'] += realized_gain['BTC'] * BitcoinPrice_query(buyList[-1]['TimeStamp'])
                sellList[-1]['Amount'] = 0
                buyList[-1]['Amount'] = 0
                break
                
            if buyList[-1]['Amount'] == 0:
                transactionFeeBTC += buyList[-1]['Fee']
                transactionFeeUSD += transactionFeeBTC * BitcoinPrice_query(buyList[-1]['TimeStamp'])
                buyList.pop()
                
        if sellList[-1]['Amount'] == 0:
            transactionFeeBTC += sellList[-1]['Fee']
            transactionFeeUSD += transactionFeeBTC * BitcoinPrice_query(sellList[-1]['TimeStamp'])
            sellList.pop()
    realized_gain['BTC'] -= transactionFeeBTC
    realized_gain['USD'] -= transactionFeeUSD
    return buyList, sellList, realized_gain

def calcUnrealizedGainLoss(key, buyList):
    cost = {'BTC': 0, 'USD': 0}
    currentValue = {'BTC': 0, 'USD': 0}
    unrealized_gain = {'BTC': 0, 'USD': 0}
    
    transactionFee = sum(item['Fee'] for item in buyList)
    # Get all outstanding amount
    totalAmount = sum(item['Amount'] for item in buyList)
    # Find needed market in all markets
    market_data = findMarketData(key, CurrentMarketsData)

    while len(buyList) > 0:
        buyTransaction = buyList.pop()
        BTCUSDPrice = BitcoinPrice_query(buyTransaction['TimeStamp'])
        unrealized_gain['BTC'] += buyTransaction['Amount'] * (market_data['LastPrice'] - buyTransaction['Rate'])
        unrealized_gain['USD'] += unrealized_gain['BTC'] * BTCUSDPrice
        cost['BTC'] += buyTransaction['Amount'] * buyTransaction['Rate']
        cost['USD'] = cost['BTC'] * BTCUSDPrice

    currentValue['BTC'] = totalAmount * market_data['LastPrice']
    currentValue['USD'] = currentValue['BTC'] * CurrentBTCUSDPrice
    unrealized_gain['BTC'] -= transactionFee
    unrealized_gain['USD'] -= transactionFee * CurrentBTCUSDPrice
    return unrealized_gain, cost, currentValue
    
def printGainLoss(key, realized, unrealized, cost, currentValue, change):
    if realized < 0 and unrealized < 0:
        print('-------------------------------------- ' + key + ' --------------------------------------')
        print('- Unrealized Gain/(Loss): (' + str('{:.9f}'.format(abs(unrealized))) + ') === Realized Gain/(Loss): (' + str('{:.9f}'.format(abs(realized))) + ') -')

    elif realized >= 0 and unrealized >= 0:
        print('-------------------------------------- ' + key + ' --------------------------------------')
        print('- Unrealized Gain/(Loss): ' + str('{:.9f}'.format(abs(unrealized))) + ' === Realized Gain/(Loss): ' + str('{:.9f}'.format(abs(realized))) + ' -')
    elif realized < 0 and unrealized >= 0:
        print('-------------------------------------- ' + key + ' --------------------------------------')
        print('- Unrealized Gain/(Loss) - ' + str('{:.9f}'.format(abs(unrealized))) + ' === Realized Gain/(Loss): (' + str('{:.9f}'.format(abs(realized))) + ') -')
    else:
        print('-------------------------------------- ' + key + ' --------------------------------------')
        print('- Unrealized Gain/(Loss): (' + str('{:.9f}'.format(abs(unrealized))) + ') --- Realized Gain/(Loss): ' + str('{:.9f}'.format(abs(realized))) + ' -')

    print('--------- Cost: ' + str('{:.9f}'.format(cost))  + '. Current Value: ' + str('{:.9f}'.format(currentValue)) + '. Change: ' + str('{:.2f}'.format(change) + '% ---------'))

def printCoinStat(entry):
    printGainLoss(entry['Market'], entry['BTC Realized Gain'], entry['BTC Unrealized Gain'], entry['BTC Cost'], entry['BTC Current Value'], entry['BTC Change'])
    printGainLoss(entry['Market'].replace('BTC','USD'), entry['USD Realized Gain'], entry['USD Unrealized Gain'], entry['USD Cost'], entry['USD Current Value'], entry['USD Change'])
    print ('---------------------------------------' + '-' * len(entry['Market']) + '---------------------------------------')
    print()

def printSummary(result):
    realized_gain = sum(item['BTC Realized Gain'] for item in result)
    unrealized_gain = sum(item['BTC Unrealized Gain'] for item in result)
    cost = sum(item['BTC Cost'] for item in result)
    currentValue = sum(item['BTC Current Value'] for item in result)
    change = ((currentValue / cost if cost != 0 else 1) - 1) * 100
    print()
    print()
    print('-------------------------------- Portfolio Summary--------------------------------')
    printGainLoss('BTC', realized_gain, unrealized_gain, cost, currentValue, change)
    
    realized_gain = sum(item['USD Realized Gain'] for item in result)
    unrealized_gain = sum(item['USD Unrealized Gain'] for item in result)
    cost = sum(item['USD Cost'] for item in result)
    currentValue = sum(item['USD Current Value'] for item in result)
    change = ((currentValue / cost if cost != 0 else 1) - 1) * 100
    printGainLoss('USD', realized_gain, unrealized_gain, cost, currentValue, change)
    print ('===============================================================================')
    
def makeFilter(a):
    def compareLabel(x):
        return a == x['Label']
    return compareLabel
def findMarketData(key, data):
    labelFilter = makeFilter(key)
    return list(filter(labelFilter, data)).pop()
def sortByValue(list):
    return sorted(list, key=lambda x:x['USD Current Value'], reverse=True)
def sortByTime(list):
    return sorted(list, key=lambda x:x['TimeStamp'], reverse=True)
    
main()