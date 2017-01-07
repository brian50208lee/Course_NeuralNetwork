function [q_val,v_val,pre_state,pre_action,cur_state,cur_action] = get_action(x,v_x,theta,v_theta,reinf,q_val,v_val,pre_state,cur_state,pre_action,cur_action,ALPHA,BETA,GAMMA)

global x_val e_val p_before BETAACE NUM_BOX

pre_state=cur_state;
pre_action=cur_action;    % Action: 1 is push left.  2 is push right
cur_state=get_box(x,v_x,theta,v_theta);

if (pre_action ~= -1)   % Update Q value. If previous action been taken
    if (cur_state == -1)  % Current state is failed
        predicted_value=0;  % fail state's value is zero
    elseif (q_val(cur_state,1)<=q_val(cur_state,2)) % Left Q<= Right Q
        predicted_value=q_val(cur_state,2);         %  set Q to bigger one
    else 
        predicted_value=q_val(cur_state,1);
    end %if
    
    % ACE
    p = v_val(pre_state,pre_action);
    reward_hat = reinf + GAMMA*p - p_before;
    v_val(pre_state,pre_action) = v_val(pre_state,pre_action) + BETAACE * reward_hat * x_val(pre_state,pre_action);
    for i=1:NUM_BOX
        for j=1:2
            x_val(i,j) = 0.8 * x_val(i,j);
        end
    end
    x_val(pre_state,pre_action) = x_val(pre_state,pre_action) + 0.2;
    p_before = p;
    
    % ASE
    %q_val(pre_state,pre_action)= q_val(pre_state,pre_action)+ ALPHA*(reinf+ GAMMA*predicted_value - q_val(pre_state,pre_action));
    % Determine best action
    if ( q_val(cur_state,1) + (rand*BETA) <= q_val(cur_state,2) )
        cur_action=2;  % push right
    else cur_action=1;  % push left
    end
    q_val(cur_state,cur_action) = q_val(cur_state,cur_action) + ALPHA * reward_hat * e_val(cur_state,cur_action);
    for i=1:NUM_BOX
        for j=1:2
            e_val(i,j) = 0.9 * e_val(i,j);
        end
    end
    e_val(cur_state,cur_action) = e_val(cur_state,cur_action) + 0.1 * (cur_action * 2 - 1);
else
    % Determine best action
    if ( q_val(cur_state,1) + (rand*BETA) <= q_val(cur_state,2) )
        cur_action=2;  % push right
    else cur_action=1;  % push left
    end  %if
    
end %if

