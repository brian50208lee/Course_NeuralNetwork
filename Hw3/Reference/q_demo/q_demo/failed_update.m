function [q_val, v_val] = failed_update(q_val, v_val, pre_state, pre_action, reinf, predicted_value)
    global ALPHA BETA GAMMA x_val e_val p_before BETAACE NUM_BOX
    %q_val(pre_state,pre_action) = q_val(pre_state,pre_action)+ ALPHA*(reinf+ GAMMA*predicted_value - q_val(pre_state,pre_action));
    
    % ACE
    p = v_val(pre_state,pre_action);
    reward_hat = -1 + GAMMA*p - p_before;
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
    if ( q_val(pre_state,1) + (rand*BETA) <= q_val(pre_state,2) )
        cur_action=2;  % push right
    else cur_action=1;  % push left
    end
    q_val(pre_state,cur_action) = q_val(pre_state,cur_action) + ALPHA * reward_hat * e_val(pre_state,cur_action);
    for i=1:NUM_BOX
        for j=1:2
            e_val(i,j) = 0.9 * e_val(i,j);
        end
    end
    e_val(pre_state,cur_action) = e_val(pre_state,cur_action) + 0.1 * (cur_action * 2 - 1);

end