#-*-coding:utf-8-*-
import time
import datetime
import timeit
import pprint

# http://docs.python.org/2/library/datetime.html#strftime-strptime-behavior

def exe_time(func):
   def new_func(*args, **args2):
      t0 = time.time()
      print "@%s, {%s} start" % (time.strftime("%X", time.localtime()), func.__name__)
      back = func(*args, **args2)
      print "@%s, {%s} end" % (time.strftime("%X", time.localtime()), func.__name__)
      print "@%.3fs taken for {%s}" % (time.time() - t0, func.__name__)
      return back
   return new_func

class TimeMeth(object):
   def __init__(self, orig_func):
      self.f = orig_func
   def __call__(self, *args, **kwargs):
      print "@%s, {%s} start" % (time.strftime("%X", time.localtime()), self.f.__name__)
      start = timeit.default_timer()
      r = self.f(*args, **kwargs)
      end = timeit.default_timer()
      print "@%s, {%s} end" % (time.strftime("%X", time.localtime()), self.f.__name__)
      print "The running time for %s:%2f"%(self.f.__name__, end-start) 
      return r
   

@exe_time
#@TimeMeth
def test():    
    ts = 1362730444.463000
    print datetime.datetime.fromtimestamp(ts)
    print datetime.datetime.fromtimestamp(ts).strftime("%Y-%m-%d %H:%M:%S.%f")
    print datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S.%f");
        
    timestr = "2013-03-08 16:14:04.463000"
    t = time.strptime(timestr, "%Y-%m-%d %H:%M:%S.%f");
    #pprint.pprint(t)
    print time.mktime(t)

    today = datetime.date.today( )
    print "today = ", today
    yesterday = today - datetime.timedelta(days=1)
    print "yesterday = ", yesterday
    tomorrow = today + datetime.timedelta(days=1)
    print "tomorrow = ", tomorrow

if __name__ == '__main__':
    test()