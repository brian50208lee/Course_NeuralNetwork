function [y, q_val] = ASE(learn, decay, reward, q_val, cur_state)
    global BETA NUM_BOX
    
    noise = rand*BETA;
    
    x = q_val(cur_state, 1) + noise;
    if (x + noise >= 0)
        y = 2;
    else
        y = 1;
    end
    
    for i = 1:NUM_BOX
        q_val(i, 1) = q_val(i, 1) + learn * reward * q_val(i, 2);
    end
    
    for i = 1:NUM_BOX
        q_val(i, 2) = decay * q_val(i, 2);
    end
    q_val(cur_state, 2) = q_val(cur_state, 2) + (1-decay) * ((y-1)*2-1);
end

