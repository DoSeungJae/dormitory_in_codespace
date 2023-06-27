import sys
import queue
input=sys.stdin.readline

n=int(input())
q=queue.Queue()
q_temp=queue.Queue()
for i in range(2,n+1,2):
    q.put(i)

if(n%2==1):
    q.put(q.get())


while(q.qsize()!=1):
    q.get()
    q.put(q.get())

print(q.get())
