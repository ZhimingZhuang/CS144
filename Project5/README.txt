Q1:
The information of the credit card number is being transported during these two communications, so we need to ensure it security. Therefore, I used SSL encryption in communication from (4) to (5) and from (5) to (6). 

Q2:
I used session to store the information of ItemId, Name and Buy Price these three attributes. Since the spec doesn’t request us to consider buying different items at the one time. I only store the information of one item in the session. In this case, the hacker cannot change the Buy_Price in the session. 