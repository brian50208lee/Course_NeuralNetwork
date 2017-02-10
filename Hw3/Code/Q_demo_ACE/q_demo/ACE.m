function [reward_hat, p ,v_val] = ACE(learn, decay, reward, gamma, p_before, v_val, cur_state)
    global NUM_BOX
    if (reward == -1) 
        p = 0;
    else 
        p = v_val(cur_state, 1);
    end
    
    
    reward_hat = reward + gamma*p - p_before;
    for i = 1:NUM_BOX
        v_val(i, 1) = v_val(i, 1) + learn * reward_hat * v_val(i, 2);
    end
    
    for i = 1:NUM_BOX
         v_val(i, 2) = decay * v_val(i, 2);
    end
    v_val(cur_state, 2) = v_val(cur_state, 2) + (1-decay);
end

