# -*- coding: utf8 -*-

'''
ioloop.py
    def start(self):
        """Starts the I/O loop.

        The loop will run until one of the I/O handlers calls stop(), which
        will make the loop stop after the current event iteration completes.
        """
        if self._stopped:
            self._stopped = False
            return
        self._running = True
        while True:
            # Never use an infinite timeout here - it can stall epoll
            poll_timeout = 0.2

            # Prevent IO event starvation by delaying new callbacks
            # to the next iteration of the event loop.
            callbacks = self._callbacks
            self._callbacks = []
            for callback in callbacks:
                self._run_callback(callback)

            if self._callbacks:
                poll_timeout = 0.0

            if self._timeouts:
                now = time.time()
                while self._timeouts:
                    if self._timeouts[0].callback is None: # 
                        # the timeout was cancelled
                        heapq.heappop(self._timeouts)
                    elif self._timeouts[0].deadline <= now:
                        timeout = heapq.heappop(self._timeouts)
                        self._run_callback(timeout.callback)
                    else:
                        milliseconds = self._timeouts[0].deadline - now
                        poll_timeout = min(milliseconds, poll_timeout)
                        break
'''

import errno
import time
from datetime import timedelta
import functools
import socket
import struct
import sys
import logging

from tornado import ioloop
from tornado import iostream


def handle_timeout(stream):
    stream.close()
    logging.debug("timeout")

def body_handler(stream, timeout, data):
    stream.io_loop.remove_timeout(timeout)
    logging.debug("%s, [%s]" % (len(data), data))
    stream.close()

def header_handler(stream, timeout, data):
    try:
        stream.io_loop.remove_timeout(timeout)
        logging.debug("%s, [%s]" % (len(data), data))
        handle_timeout_callback = functools.partial(handle_timeout, stream)
        t = stream.io_loop.add_timeout(time.time() + 50, handle_timeout_callback)
        callback = functools.partial(body_handler, stream, t)
        stream.read_bytes(5, callback)
    except Exception, e:
        logging.exception(e)


def connection_ready(sock, fd, events):
    while True:
        try:
            conn, addr = sock.accept()
            logging.debug("addr=%s" % (str(addr)))
        except socket.error, e:
            if e.args[0] in (errno.EWOULDBLOCK, errno.EAGAIN):
                return
            raise
        try:
            stream = iostream.IOStream(conn)
            handle_timeout_callback = functools.partial(handle_timeout, stream)
            t = stream.io_loop.add_timeout(time.time() + 5, handle_timeout_callback)
            callback = functools.partial(header_handler, stream, t)
            stream.read_bytes(5, callback)
        except Exception, e:
            logging.exception(e)


def sock_server(sock_host, sock_port):
    try:
        server_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        server_sock.setblocking(0)
        server_sock.bind((sock_host, sock_port))
        server_sock.listen(300)

        io_loop = ioloop.IOLoop.instance()
        callback = functools.partial(connection_ready, server_sock)
        io_loop.add_handler(server_sock.fileno(), callback, io_loop.READ)
        io_loop.start()

    except Exception, e:
        logging.exception(e)
        io_loop.stop()


def main():
    logging.basicConfig(level=logging.DEBUG)
    try:
        sock_server("0.0.0.0", 9999)
    except Exception, e:
        logging.exception(e)


if __name__ == "__main__":
    main()
