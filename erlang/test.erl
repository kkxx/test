-module(test).
-export([start/0, say_something/2]).

say_something(What, 0) ->
    io:format("~p ---end--- ~n", [What]),
    done;

say_something(What, Times) ->
    io:format("~p~n", [What]),
    say_something(What, Times - 1).

start() ->
    io:format("---Hello world---~n"),
    spawn(test, say_something, [hello, 3]),
    spawn(test, say_something, [goodbye, 3]).
