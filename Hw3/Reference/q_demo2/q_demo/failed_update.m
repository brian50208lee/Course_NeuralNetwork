function [q_val, v_val] = failed_update(q_val, v_val, pre_state, pre_action, reinf, predicted_value)
global GAMMA

% Determine best action
global p_before BETAACE 
[reward_hat, p_before, v_val] = ACE(BETAACE, 0.8, -1, GAMMA, p_before, v_val, pre_state);
[cur_action, q_val] = ASE(1000, 0.9, reward_hat, q_val, pre_state);

%q_val(pre_state,pre_action) = q_val(pre_state,pre_action)+ ALPHA*(reinf+ GAMMA*predicted_value - q_val(pre_state,pre_action));
end