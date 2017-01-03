function [pre_state,cur_state,pre_action,cur_action,x,v_x,theta,v_theta] = reset_cart(BETA)  % reset the cart pole to initial state
pre_state=1;
cur_state=1;
pre_action=-1;  % -1 means no action been taken
cur_action=-1;
x=rand*BETA;     % the location of cart
v_x=rand*BETA;   % the velocity of cart
theta=rand*BETA;   %the angle of pole
v_theta=rand*BETA;    %the velocity of pole angle
